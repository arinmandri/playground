const REF_PREFIX = '#/components/schemas/';

export default function exportSchemas(schemas) {
  let result = '';

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

  //// 말단 원시 타입
  if (directType === 'number') return 'number';
  if (directType === 'integer') return 'number';
  if (directType === 'string') return 'string';

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

    result += `  ${pName}${isRequired ? '' : '?'}: ${tsType},\n`;
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