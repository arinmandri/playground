const REF_PREFIX = '#/components/schemas/';

/**
 * {
 *   "enumType1": ['val1', 'val2', 'val3', ...],
 *   "enumType2": ['val4', 'val5', 'val6', ...],
 *   ...
 * }
 */
let predefinedEnums;
let indent;

export default function exportSchemas(schemas, predefinedEnums_, indent_) {
  predefinedEnums = predefinedEnums_;
  indent = indent_;

  let result = '';

  result += exportPredefinedEnums();

  for (let schemaName in schemas) {
    const schema = schemas[schemaName];

    try {
      result += `export interface ${schemaName} ${getRootSchemaType(schema)}\n\n`;
    } catch (err) {
      throw new Error(`타입 ${schemaName} 이가 문제.\n` + err.message);
    } finally {
    }
  }

  return result;
}

function getRootSchemaType(schema) {// 이 백엔드에서 모든 Request Body, Response Body는 오브젝트 타입이다.
  if (schema.type === 'object')
    return getTsType(schema);

  const extendedType = schema['allOf'];
  if (extendedType !== undefined) {
    return extractExtedType(extendedType);
  }

  if (schema.discriminator !== undefined) {
    return extractObjType(schema);
  }

  throw new Error('이 스키마의 타입을 모르겠어요.', schema);
}

function extractExtedType(extendedType) {
  const parentType = extractRefType(extendedType[0]['$ref']);
  const childType = getTsType(extendedType[1]);
  return 'extends ' + parentType + ' ' + childType;
}

function getTsType(schema) {
  const directType = schema.type;

  //// 말단 타입
  if (directType === 'number') return 'number';
  if (directType === 'integer') return 'number';
  if (directType === 'string') {
    //// enum or string
    const enumv = schema['enum'];
    if (enumv !== undefined) {
      return extractEnumType(enumv);
    }

    return 'string';
  }

  //// 배열
  if (directType === 'array') {
    const itemType = schema.items
    return '(' + getTsType(itemType) + ')[]';
  }

  //// 오브젝트
  if (directType === 'object') {
    return extractObjType(schema);
  }

  //// 특수
  if (directType === undefined) {

    const refType = schema['$ref'];
    if (refType !== undefined) {
      return extractRefType(refType);
    }

    const unionType = schema['oneOf'];
    if (unionType !== undefined) {
      return extractUnionType(unionType);
    }

    throw new Error('필드 타입이 없다. ' + JSON.stringify(schema));
  }

  throw new Error('모르는 필드 타입 ' + JSON.stringify(schema));
}

/**
 * enumv ['e1', 'e2', ...]
 */
function extractEnumType(enumv) {
  const found = findEnumType(enumv);
  return found || enumv.map(e => `'${e}'`).join(' | ');
}

function findEnumType(enumv) {
  //// 순서대로 모든 요소 맞으면 같은 enum이다.
  for (let eName in predefinedEnums) {
    const predefinedEnum = predefinedEnums[eName];

    //// 값 개수 일치 확인
    if (enumv.length !== predefinedEnum.length)
      return null;

    //// 각 항목 일치 확인
    let good = true;
    for (let i = 0; i < enumv.length; i++) {
      if (enumv[i] !== predefinedEnum[i]) {
        good = false;
        break;
      }
    }
    if (good === true)
      return eName;
  }
  return null;
}

function extractObjType(schema) {
  let result = '{\n';

  const properties = schema.properties;
  const required = schema.required;
  for (let pName in properties) {
    const property = properties[pName];
    const tsType = getTsType(property);

    let isRequired = required === undefined
      ? true
      : required.includes(pName) ? true : false
      ;

    result += `${indent}${pName}${isRequired ? '' : '?'}: ${tsType},\n`;
  }

  return result + '}';
}

function extractRefType(refType) {
  if (refType.startsWith(REF_PREFIX)) {
    return refType.slice(REF_PREFIX.length);
  } else {
    throw new Error(`이상한 참조필드 타입: '${refType}'이가 '${REF_PREFIX}'로 시작을 안 함.`);
  }
}

function extractUnionType(unionType) {
  return unionType.map((unionItem) => getTsType(unionItem)).join(' | ');
}

function exportPredefinedEnums() {
  let result = '';

  for (let eName in predefinedEnums) {
    const predefinedEnum = predefinedEnums[eName];
    result += 'export enum ' + eName + ' {\n';
    for (let eVal of predefinedEnum) {
      result += `  ${eVal} = '${eVal}',\n`;
    }
    result += '}\n\n';
  }

  return result;
}
