const API_DOCS_URL = 'http://localhost:3000/v3/api-docs/for-client';
const EXPORT_PATH = 'src/api/';
const INDENT = '  ';

import fs from "fs";
import { init, exportSchemas, exportPaths } from "./fetchDocs-export.js";

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

    const predefinedEnums = JSON.parse(fs.readFileSync('src/api/schemas-predefined-enums.json', 'utf8'));
    init( predefinedEnums, INDENT );

    fs.writeFileSync( EXPORT_PATH + "/schemas.ts",
        '/*\nnpm run api 자동생성\n*/\n\n'
        + exportSchemas( data.components.schemas )
        + fs.readFileSync('src/api/schemas-additionals.ts', 'utf8')
    );

    fs.writeFileSync( EXPORT_PATH + "/operations.ts",
        '/*\nnpm run api 자동생성\n*/\n\n'
        + exportPaths( data.paths )
    );

    console.log('\nFETCH API DOCS DONE.');
  }catch( err ){
    console.error("요청 또는 저장 실패:", err);
  }
}

main();
