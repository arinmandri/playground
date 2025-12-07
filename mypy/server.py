import os, shutil, tempfile, zipfile

from flask import Flask, request, send_file, abort
from werkzeug.utils import secure_filename

from ipynb_to_md import ipynb_to_md

app = Flask(__name__)

@app.route('/ipynb-to-md', methods=['POST'])
def ipynb_to_md_endpoint():
    # 1. 입력 검증
    if 'file' not in request.files:
        abort(400, 'ipynb file is required (multipart/form-data, key="file")')

    file = request.files['file']
    if file.filename == '':
        abort(400, 'empty filename')

    filename = secure_filename(file.filename)
    if not filename.endswith('.ipynb'):
        abort(400, 'only .ipynb files are supported')

    # 2. 작업용 임시 디렉토리 생성
    work_dir = tempfile.mkdtemp(prefix='ipynb_to_md_')
    src_path = os.path.join(work_dir, filename)
    base_name = os.path.splitext(filename)[0]

    try:
        # 3. ipynb 저장
        file.save(src_path)

        # 4. 변환 결과 디렉토리
        output_dir = os.path.join(work_dir, 'output')
        os.makedirs(output_dir, exist_ok=True)

        target_md_path = os.path.join(output_dir, f'{base_name}.md')

        # 5. ipynb → md 변환
        ipynb_to_md(
            src_path=src_path,
            target_path=target_md_path,
            export_images=True,
            export_images_path=None,
        )

        # 6. zip 생성
        zip_path = os.path.join(work_dir, f'{base_name}.zip')
        with zipfile.ZipFile(zip_path, 'w', zipfile.ZIP_DEFLATED) as zf:
            for root, _, files in os.walk(output_dir):
                for f in files:
                    full_path = os.path.join(root, f)
                    rel_path = os.path.relpath(full_path, output_dir)
                    zf.write(full_path, rel_path)

        # 7. zip 파일 응답 (다운로드)
        return send_file(
            zip_path,
            as_attachment=True,
            download_name=f'{base_name}.zip',
            mimetype='application/zip'
        )

    finally:
        # 8. 임시 디렉토리 정리
        shutil.rmtree(work_dir, ignore_errors=True)


if __name__ == '__main__':
    app.run(host='127.0.0.1', port=5000)
