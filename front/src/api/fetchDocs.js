const API_DOCS_URL = 'http://localhost:3000/v3/api-docs/for-client';
const EXPORT_PATH = 'src/api/';
const INDENT = '  ';

import fs from "fs";
import exportSchemas from "./fetchDocs-exportSchemas.js";

async function main() {
  try {
    const response = await fetch(API_DOCS_URL);
    if (!response.ok) {
      throw new Error(`뭔가 안 됨. (${response.status})`);
    }

    const data = await response.json();

    console.log('++++++++++++++++++++++++++++++++++++++++');
    console.log( API_DOCS_URL );
    console.log('★ OPENAPI ' + data.openapi);
    console.log('★ BACKEND API: ' + JSON.stringify(data.info, null, 2));
    console.log('++++++++++++++++++++++++++++++++++++++++');

    const schemas = data.components.schemas;
    const predefinedEnums = JSON.parse(fs.readFileSync('src/api/schemas-predefined-enums.json', 'utf8'));
    fs.writeFileSync(EXPORT_PATH + "/schemas-raw.json", JSON.stringify(schemas, null, 2));
    fs.writeFileSync(EXPORT_PATH + "/schemas.ts",
        '/*\nnpm run api 자동생성\n*/\n\n'
        + exportSchemas(schemas, predefinedEnums, INDENT)
        + fs.readFileSync('src/api/schemas-additionals.ts', 'utf8')
    );

    // TODO
    const paths = data.paths;
    // fs.writeFileSync(EXPORT_PATH + "/paths-raw.json", JSON.stringify(paths, null, 2));
    // fs.writeFileSync(EXPORT_PATH + "/paths.ts", '/*\nnpm run api 자동생성\n*/\n\n' + exportPaths(paths));

    console.log('\nFETCH API DOCS DONE.');
  } catch (err) {
    console.error("요청 또는 저장 실패:", err);
  }
}

main();
