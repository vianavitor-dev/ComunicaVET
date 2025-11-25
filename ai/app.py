import os
from flask import Flask, request, jsonify
from gemini_integration import GeminiAIService

app = Flask(__name__)
ai_service = GeminiAIService()

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({"status": "healthy", "gemini_available": ai_service.is_available})

@app.route('/recommendations', methods=['POST'])
def get_recommendations():
    data = request.get_json()
    if not data:
        return jsonify({"error": "Dados JSON necessários"}), 400

    try:
        recommendations = ai_service.get_recommendations(data)
        return jsonify(recommendations)
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port)