const API_DOCS_URL = 'http://localhost:3000/v3/api-docs/for-client';
const EXPORT_PATH = 'src/api/';

import fs from "fs";
import exportSchemas from "./api-fetchDocs-exportSchemas.js";

const predefinedEnums = JSON.parse(fs.readFileSync('src/api/enums.json', 'utf8'));

async function main() {
  try {
    const response = await fetch(API_DOCS_URL);
    if (!response.ok) {
      throw new Error(`뭔가 안 됨. (${response.status})`);
    }

    const data = await response.json();

    console.log('++++++++++++++++++++++++++++++++++++++++');
    console.log('★ OPENAPI ' + data.openapi);
    console.log('★ BACKEND API: ' + JSON.stringify(data.info, null, 2));
    console.log('++++++++++++++++++++++++++++++++++++++++');

    const schemas = data.components.schemas;
    fs.writeFileSync(EXPORT_PATH + "/api-schemas-raw.json", JSON.stringify(schemas, null, 2));
    fs.writeFileSync(EXPORT_PATH + "/api-schemas.ts", '/*\nnpm run api 자동생성\n*/\n\n' + exportSchemas(schemas, predefinedEnums));

    // TODO
    const paths = data.paths;
    // fs.writeFileSync(EXPORT_PATH + "/api-paths-raw.json", JSON.stringify(paths, null, 2));
    // fs.writeFileSync(EXPORT_PATH + "/api-paths.ts", '/*\nnpm run api 자동생성\n*/\n\n' + exportPaths(paths));

    console.log('\nFETCH API DOCS DONE.');
  } catch (err) {
    console.error("요청 또는 저장 실패:", err);
  }
}

main();
