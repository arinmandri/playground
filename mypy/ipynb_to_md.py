"""
https://peekabook.tistory.com/entry/ipynb-to-markdown

다른 파이썬 파일에서
from tomd import ipynb_to_md

터미널에서
python tomd.py ./something.ipynb
python tomd.py ./something.ipynb ./haha.md
"""

import os, sys
from pathlib import Path
import json, re, base64

ANSI_COLOR_MAP = {
    '30': 'black',
    '31': 'red',
    '32': 'green',
    '33': 'yellow',
    '34': 'blue',
    '35': 'purple',
    '36': 'cyan',
    '37': 'white',
    '90': 'gray',
    '91': 'lightcoral',
    '92': 'lightgreen',
    '93': 'lightyellow',
    '94': 'lightblue',
    '95': 'plum',
    '96': 'lightcyan',
    '97': 'white',
}
ANSI_BACKCOLOR_MAP = {
    '40': 'black',
    '41': 'red',
    '42': 'green',
    '43': 'yellow',
    '44': 'blue',
    '45': 'purple',
    '46': 'cyan',
    '47': 'white',
}

def ansi_to_html(text):
    span_count = 0

    def replace_ansi(match):
        nonlocal span_count

        codes = match.group(1).split(';')
        styles = []
        content = ''

        for code in codes:
            if code == '0':  # reset
                for _ in range(span_count):
                    content += '</span>'
                span_count = 0
            elif code in ANSI_COLOR_MAP:
                styles.append(f"color: {ANSI_COLOR_MAP[code]}")
            elif code in ANSI_BACKCOLOR_MAP:
                styles.append(f"background-color: {ANSI_BACKCOLOR_MAP[code]}")
            elif code == '1':
                styles.append("font-weight: bold")
            elif code == '21':
                styles.append("font-weight: normal")
            elif code == '4':
                styles.append("text-decoration: underline")
            elif code == '24':
                styles.append("text-decoration: none")

        if styles:
            content += f'<span style="{"; ".join(styles)}">'
            span_count += 1

        return content

    html = re.sub(r'\x1B\[([0-9;]+)m', replace_ansi, text)

    for _ in range(span_count):
        html += '</span>'

    return html

def create_directory(dir):
    try:
        if not os.path.exists(dir):
            os.makedirs(dir)
    except OSError:
        print('[ERROR] Failed to create the directory.')

def save_base64_png(output_path: str|Path, base64_png: str):

    if base64_png.startswith("data:image"):
        base64_png = base64_png.split(",", 1)[1]

    image_bytes = base64.b64decode(base64_png)

    with open(output_path, "wb") as f:
        f.write(image_bytes)

# 참고: https://ipython.org/ipython-doc/3/notebook/nbformat.html

def ipynb_to_md(
        src_path: str,
        target_path: str|None =None,
        *,
        make_dir=True,
        overwrite=True,
        include_md=True,
        include_code=True,
        include_output=True,
        include_output_all=False,
        include_output_text=True,
        include_output_html=False,
        include_output_png=True,
        export_images=True,
        export_images_path: str|None=None,
        include_output_json=True,
        json_indent_size: int =4,
        include_output_error=False,
        include_output_error_as_html=False,
) -> None:
    """
    Jupyter Notebook 파일을 Markdown 파일로 변환하여 저장한다.
    파라미터에 따라 마크다운 셀, 코드 셀, 다양한 종류의 출력 부분을 선택적으로 포함한다.

    Args:
        src_path (str): 변환할 Jupyter Notebook 파일의 경로
        target_path (str | None): 출력할 Markdown 파일의 경로. (기본값: None)
                                  None: src_path와 같은 이름의 .md 파일 생성
        make_dir (bool): target_path의 부모 디렉토리가 없으면 생성할지 여부 (기본값: True)
        overwrite (bool): 기존 파일을 덮어쓸지 여부 (기본값: True)
                          True: 같은 이름의 파일이 존재하면 삭제하고 덮어쓴다.
                          False: 같은 이름의 파일이 존재하면 변환이 실행되지 않는다.
                          이미지 파일을 내보낼 경우; 이미지 파일을 덮어쓸지도 함께 설정한다.
        include_md (bool): 마크다운 셀을 포함할지 여부 (기본값: True)
        include_code (bool): 코드 셀을 포함할지 여부 (기본값: True)
        include_output (bool): 셀 실행 결과를 포함할지 여부 (기본값: True)
                               False: include_output_* 패턴의 모든 파라미터들이 무시된다.
        include_output_all (bool): 모든 출력 형식을 포함할지 여부 (기본값: False)
                                   True: include_output_* 패턴의 다른 모든 파라미터들이 무시된다.
        include_output_text (bool): 텍스트 스트림 출력을 포함할지 여부 (기본값: True)
        include_output_html (bool): HTML 출력을 포함할지 여부 (기본값: False)
        include_output_png (bool): PNG 이미지 출력을 포함할지 여부 (기본값: True)
        export_images (bool): 출력된 이미지를 별도 파일로 내보낼지 여부.
                              True: 이미지를 별도 디렉토리에 저장하고 ![Image](images/img-1.png) 식으로 참조한다.
                              False: img 태그를 출력하며 그 태그에 바로 base64 데이터를 넣는다.
        export_images_path (str | None): export_images가 True일 때 각 이미지 파일의 이름 패턴. (기본값: None)
                                         예) "images/img-*"이면 각 이미지가 "images" 디렉토리 하위 "img-1.png", "img-2.png" 식의 이름으로 저장된다.
                                         target_path 의 부모 디렉토리 기준 상대경로이다.
                                         '*'는 이미지 번호로 대체된다. '*'가 없으면 자동으로 끝에 '-*'가 붙는다.
                                         ':'는 md 파일 이름(확장자명 제외)으로 대체된다.
                                         None: ':-img-*'
        include_output_json (bool): JSON 출력을 포함할지 여부 (기본값: True)
        json_indent_size (int): JSON 출력의 들여쓰기 크기 (기본값: 4)
        include_output_error (bool): 에러 출력을 포함할지 여부 (기본값: False)
        include_output_error_as_html (bool): 에러를 HTML 형식으로 출력할지 여부 (기본값: False)
                                             HTML 형식에서는 ANSI Escape Code를 처리하여 글자색 등이 보존된다.
                                             False: ANSI Escape Code를 모두 삭제하고 보통의 텍스트와 같은 방식으로 출력한다.
    Returns:
        None
    Raises:
        FileNotFoundError: src_path 파일이 존재하지 않을 경우
    """
    if include_output_all:
        include_output_text = True
        include_output_html = True
        include_output_png = True
        include_output_json = True
        include_output_error = True
        include_output_error_as_html = True

    image_number = 0

    src_path_ = Path(src_path)
    if not src_path_.exists():
        raise FileNotFoundError(f'[ERROR] 파일을 찾을 수 없습니다: {src_path_}')

    # 출력 경로
    if target_path is None:# 기본 출력 경로: 같은 이름의 md 파일
        target_path = str(src_path_.with_suffix('.md'))
    target_path_ = Path(target_path)
    if target_path_.exists() and not overwrite:
        print(f'[ERROR] {target_path} 파일이 이미 존재하여 덮어쓰지 않습니다.')
        return
    target_dir_ = target_path_.parent
    if make_dir:
        create_directory(target_dir_)

    # 이미지 출력 경로
    if export_images_path is None:# 기본 이미지 디렉토리
        export_images_path = ':-img-*'
    if '*' not in export_images_path:
        export_images_path = export_images_path + '-*'
    export_images_path = export_images_path.replace(':', target_path_.stem)
    # 디렉토리 생성은 실제 이미지를 내보낼 때


    def f_text_cell(text: str|list[str], code_type: str ='', sep_nl=False) -> str:
        sep = '\n' if sep_nl else ''
        content = sep.join(text).strip()
        return '```' + code_type + '\n' + content + '\n```'

    def f_base64_to_img_tag(base64_data: str, img_type: str) -> str:
        """
        base64 --> img tag
        """
        return f'<img src="data:image/{img_type};base64,{base64_data}">\n'

    def f_filepath_to_img_link(path: str|Path) -> str:
        return f'![Image]({path})'

    def cell_to_md(cell) -> str|None:
        source = cell.get('source')
        if source:
            return ''.join(source)

    def cell_to_code(cell) -> str|None:
        source_list = cell.get('source')  # single string | list of strings
        if source_list:
            if type(source_list) == 'str':
                source_list = [source_list]
            code = ''.join(source_list)
            return f_text_cell(code, 'py')

    def outputdata_to_img_tag_png(data) -> str|None:
        data_png_list = data.get('image/png')  # ["base64-encoded-png-data"]
        if data_png_list:
            if type(data_png_list) == str: data_png_list = [data_png_list]
            content = ''
            for data_png in data_png_list:
                content += f_base64_to_img_tag(data_png, 'png')
            return content

    def outputdata_to_img_link_png(data) -> str|None:
        nonlocal image_number, export_images_path

        data_png_list = data.get('image/png')  # ["base64-encoded-png-data"]
        if data_png_list:
            if type(data_png_list) == str: data_png_list = [data_png_list]
            content = ''
            for data_png in data_png_list:
                image_number += 1
                path = export_images_path.replace('*', str(image_number)) + '.png'
                path_ = Path(target_dir_, path)
                if path_.exists() and not overwrite:
                    raise Exception(f'이미지 {path_}가 이미 있음.')
                if not path_.parent.exists():
                    create_directory(path_.parent)
                save_base64_png(path_, data_png)
                content += f_filepath_to_img_link(path)
            return content

    def outputdata_to_json(data, json_indent_size) -> str|None:
        data_json = data.get('application/json')  # {"json": "data", ...} # JSON data is included as-is
        if data_json:
            content = f_text_cell(json.dumps(data_json, indent=json_indent_size), 'json')
            return content

    def outputdata_to_html(data, to_html) -> str|None:
        if to_html:
            data_html = data.get('text/html')
            if data_html:
                return ''.join(data_html)
        else:
            data_text = data.get('text/plain')
            if data_text:
                return f_text_cell(data_text, 'text')

    def output_to_text(output, as_html) -> str|None:
        content = None
        traceback = output.get('traceback')  # list
        if traceback:
            text = "\n".join(traceback).strip()
            if as_html:
                text = ansi_to_html(text)
                text = re.sub(r'\x1B\[[0-?]*[ -/]*[@-~]', '', text)  # ANSI escape code 모두 제거
                content = f'<pre>{text}\n</pre>'
            else:
                text = re.sub(r'\x1B\[[0-?]*[ -/]*[@-~]', '', text)  # ANSI escape code 모두 제거
                content = f_text_cell(text)
        return content

    def fw(content):
        nonlocal f

        blank_between_cells = '\n\n'
        if content:
            f.write(content)
            f.write(blank_between_cells)

    # 소스 읽기
    with open(src_path_, 'r', encoding='utf-8') as f:
        notebook = json.load(f)

    with open(target_path, 'w', encoding='utf-8') as f:
        for cell in notebook.get('cells', []):
            cell_type = cell.get('cell_type')

            if cell_type == 'markdown':
                if include_md:
                    content = cell_to_md(cell)
                    fw(content)

            elif cell_type == 'code':
                if include_code:
                    content = cell_to_code(cell)
                    fw(content)

                if include_output:
                    outputs = cell.get('outputs', [])
                    for output in outputs:
                        output_type = output.get('output_type')

                        if output_type == 'stream':
                            if include_output_text:
                                text = output.get('text')  # ["multiline stream text"]
                                content = f_text_cell(text, 'text')
                                fw(content)

                        elif output_type == 'display_data' or output_type == 'execute_result':
                            data = output.get('data')
                            if data:
                                content = None

                                if include_output_png:
                                    if export_images:
                                        content = outputdata_to_img_link_png(data)
                                    else:
                                        content = outputdata_to_img_tag_png(data)
                                    fw(content)

                                if include_output_json:
                                    content = outputdata_to_json(data, json_indent_size)
                                    fw(content)

                                if content == None:
                                    if include_output_html or include_output_text:
                                        content = outputdata_to_html(data, include_output_html)
                                        fw(content)

                        elif output_type == 'error':
                            if include_output_error:
                                content = output_to_text(output, include_output_error_as_html)
                                fw(content)

                        else:
                            print(f'[WARN] ({cell.get("id")}) Unknown output type: {output_type}')

            else:
                print(f'[WARN] ({cell.get("id")}) Unknown cell type: {cell_type}')
                continue

    print(f'변환 완료: `{src_path_}` --> `{target_path}`')

if __name__ == '__main__' :
    argv = sys.argv
    ipynb_to_md(argv[1], argv[2])
