const REF_PREFIX = '#/components/schemas/';

/**
 * {
 *   "enumType1": ['val1', 'val2', 'val3', ...],
 *   "enumType2": ['val4', 'val5', 'val6', ...],
 *   ...
 * }
 */
let predefinedEnums;
let ____;// indent string
const allCustomTypes = [];// TODO additionals

export function init( predefinedEnums_ , indent ){
  predefinedEnums = predefinedEnums_;
  ____ = indent;
}

export function exportSchemas( schemas ){

  let result = '';

  result += exportPredefinedEnums();

  for( const schemaName of Object.keys(schemas).sort((a, b) => a.localeCompare(b)) ){// for(let schemaName in schemas) 대신 알파벳순
    allCustomTypes.push( schemaName );
    const schema = schemas[schemaName];

    try {
      result += `export interface ${schemaName} ${getRootSchemaType(schema)}\n\n`;
    }catch( err ){
      throw new Error(`타입 ${schemaName} 이가 문제.\n` + err.message);
    }
  }

  return result;
}

function getRootSchemaType( schema ){// 이 백엔드에서 모든 Request Body, Response Body는 오브젝트 타입이다.
  if( schema.type === 'object' )
    return getTsType(schema);

  const extendedType = schema['allOf'];
  if( extendedType !== undefined ){
    return extractExtedType(extendedType);
  }

  if( schema.discriminator !== undefined ){
    return extractObjType(schema);
  }

  throw new Error( '이 스키마의 타입을 모르겠어요.', schema );
}

function extractExtedType( extendedType ){
  const parentType = extractRefType( extendedType[0]['$ref'] );
  const childType = getTsType( extendedType[1] );
  return 'extends ' + parentType + ' ' + childType;
}

function getTsType( schema ){
  const directType = schema.type;

  //// 말단 타입
  if( directType === 'number' ) return 'number';
  if( directType === 'integer' ) return 'number';
  if( directType === 'string' ){
    //// enum or string
    const enumv = schema['enum'];
    if( enumv !== undefined ){
      return extractEnumType( enumv );
    }

    return 'string';
  }

  //// 배열
  if( directType === 'array' ){
    const itemType = schema.items
    return '(' + getTsType( itemType ) + ')[]';
  }

  //// 오브젝트
  if( directType === 'object' ){
    return extractObjType( schema );
  }

  //// 특수
  if( directType === undefined ){

    const refType = schema['$ref'];
    if( refType !== undefined ){
      return extractRefType( refType );
    }

    const unionType = schema['oneOf'];
    if( unionType !== undefined ){
      return extractUnionType( unionType );
    }

    throw new Error('필드 타입이 없다. ' + JSON.stringify( schema ));
  }

  throw new Error('모르는 필드 타입 ' + JSON.stringify( schema ));
}

/**
 * enumv ['e1', 'e2', ...]
 */
function extractEnumType( enumv ){
  const found = findEnumType(enumv);
  return found || enumv.map(e => `'${e}'`).join(' | ');
}

function findEnumType( enumv ){
  //// 순서대로 모든 요소 맞으면 같은 enum이다.
  for( let eName in predefinedEnums ){
    const predefinedEnum = predefinedEnums[eName];

    //// 값 개수 일치 확인
    if( enumv.length !== predefinedEnum.length )
      return null;

    //// 각 항목 일치 확인
    let good = true;
    for( let i = 0; i < enumv.length; i++ ){
      if( enumv[i] !== predefinedEnum[i] ){
        good = false;
        break;
      }
    }
    if( good === true )
      return eName;
  }
  return null;
}

function extractObjType(schema ){
  let result = '{\n';

  const properties = schema.properties;
  const required = schema.required;
  for( let pName of Object.keys(properties).sort((a, b) => a.localeCompare(b)) ){// for(let pName in properties) 대신 알파벳순
    const property = properties[pName];
    const tsType = getTsType(property);

    let isRequired = required === undefined
      ? true
      : required.includes(pName) ? true : false
      ;

    result += `${____}${pName}${isRequired ? '' : '?'}: ${tsType},\n`;
  }

  return result + '}';
}

function extractRefType( refType ){
  if( refType.startsWith( REF_PREFIX ) ){
    const customType = refType.slice( REF_PREFIX.length );// '#/components/schemas/MyType' --> 'MyType'
    return customType;
  }else{
    throw new Error(`이상한 참조필드 타입: '${refType}'이가 '${REF_PREFIX}'로 시작을 안 함.`);
  }
}

function extractUnionType( unionType ){
  return unionType.map(
    ( unionItem )=> getTsType(unionItem)
  ).join(' | ');
}

function exportPredefinedEnums(){
  let result = '';

  for( let eName in predefinedEnums ){
    const predefinedEnum = predefinedEnums[eName];
    result += 'export enum ' + eName + ' {\n';
    for( let eVal of predefinedEnum ){
      result += `${____}${eVal} = '${eVal}',\n`;
    }
    result += '}\n\n';
  }

  return result;
}

//// ****************************************************************************
//// ****************************************************************************
//// ****************************************************************************

const opIdMap = new Map();

export function exportPaths( paths ) {

  let result = '';
  result += 'import api from "@/api/api";\n';
  result += `import type { ${allCustomTypes.join(', ')} } from "@/api/schemas";\n`;
  result += '\n\n\n';

  for( const path in paths ){
    const methods = paths[path];
    for( const method in methods ){
      result += `// [${method.toUpperCase()}] ${path}\n`;// [POST] /api/example
      const op = methods[method];

      const opId = op.operationId;
      if( opIdMap.has(opId) )
        throw new Error('operationId 중복 :' + opId);// TODO test

      //// response type
      const responses = op.responses;
      const response200 = responses['200'];
      const returnType = ( response200.content !== undefined )
          ? getTsType( response200.content['application/json;charset=UTF-8'].schema )// 나는 모든 API를 이 형식으로 통일함.
          : 'void';

      if( method === 'get' ){
        result += createGetFunctionStr( opId, path, returnType, op.parameters );
      }
      else if( method === 'post' ){
        result += createPostFunctionStr( opId, path, returnType, op.parameters, op.requestBody );
      }
    }
  }

  return result;
}

function createGetFunctionStr( opId, path, returnType, parameters ){
  let result = '';

  //// Parameters
  const [ pathParams, queryParams ] = classifyParams(parameters);
  const functionParams = createParamStrs( parameters );
  const pathStr = createPathStr( path, pathParams );

  result += `export async function ${opId}( ${functionParams.join(' , ')} )`
  result += `: Promise<${returnType}> {\n`;
  result += ____ + 'const params = {\n';
  for( const qp of queryParams ){
    result += ____ + ____ + `${qp.name},\n`;
  }
  result += ____ + '};\n';
  result += ____ + 'const response = await api.get('
                 + `\`${pathStr}\`, params`
                 + ');\n';
  result += ____ + `return response.data;\n`;
  result += '}\n\n';
  return result;
}

function createPostFunctionStr( opId, path, returnType, parameters, requestBody ){
  let result = '';

  //// Parameters
  const [ pathParams, _ ] = classifyParams( parameters );
  const functionParams = createParamStrs( parameters );
  const pathStr = createPathStr( path, pathParams );

  //// Request Body
  let hasDataParam = false;
  if( requestBody !== undefined ){
    hasDataParam = true;
    const reqBodyType = getTsType( requestBody.content['application/json;charset=UTF-8'].schema );// 나는 모든 API를 이 형식으로 통일함.
    functionParams.push( `data: ${reqBodyType}` );
  }

  result += `export async function ${opId}( ${functionParams.join(' , ')} )`
  result += `: Promise<${returnType}> {\n`;
  result += ____ + 'const response = await api.post('
                 + `\`${pathStr}\`${hasDataParam ? ', data':''}`
                 + ');\n';
  result += ____ + `return response.data;\n`;
  result += '}\n\n';
  return result;
}

/**
 * @param parameters OpenAPI parameters
 * @returns [ in 값에 따라 분리, ... ]
 */
function classifyParams( parameters ){
  const pathParams = [];
  const queryParams = [];
  if( Array.isArray(parameters) ){
    for( const p of parameters ){
      if( p.in == 'path' )
        pathParams.push( p );
      else if( p.in == 'query' )
        queryParams.push( p );
      else
        console.warn('파라미터 위치 ' + p.in + '이가 뭐임?');
    }
  }
  return [ pathParams, queryParams ];
}

/**
 * @param params OpenAPI parameters
 * @returns 예: ["param1: type1", "member_id: number"]
 */
function createParamStrs( params ){
  if( params == null ) return [];

  const result = [];

  for( const param of params ){
    const paramName1 = param.name.replaceAll('-', '_');
    const paramType = getTsType( param.schema );
    if( param.required ){
      result.push(`${paramName1}: ${paramType}`);
    }else{
      result.push(`${paramName1}: (${paramType})|null`);
    }
  }

  return result;
}

/**
 * @param path       operation path (예: "/path1/{param1}/{membe-id}")
 * @param pathParams OpenAPI parameters 중에 in="paths"인 것들만.
 * @returns 예: "/path1/${param1}/${membe_id}"
 */
function createPathStr( path , pathParams ){

  for( const param of pathParams ){
    const paramName0 = param.name;
    const paramName1 = param.name.replaceAll('-', '_');
    path = path.replaceAll(
      '{' + paramName0 + '}',
      '${' + paramName1 + '}'
    );
  }
  return path;
}
