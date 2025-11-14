from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/ipyno-to-md', methods=['POST'])
def hello():
    # TODO 이거 md로 ... 흑
    data = request.json
    param1 = data.get('param1', '')
    return f"world {param1}", 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
