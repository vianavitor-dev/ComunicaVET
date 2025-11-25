\
        # 🐾 Pet AI Recommendation - Serviço Gratuito

        Serviço de IA para recomendações personalizadas de cuidados para pets, **100% gratuito** (com fallback local).

        ## Notas importantes
        - Por segurança o arquivo `.env` no pacote está **SEM** a chave GEMINI_API_KEY.
        - Se você quiser usar a integração com Gemini, adicione sua chave no `.env`:
          `GEMINI_API_KEY=YOUR_KEY_HERE`
        - Instale as dependências com `pip install -r requirements.txt`

        ## 🚀 COMEÇAR RÁPIDO

        ### Linux/Mac:
        ```bash
        chmod +x start.sh
        ./start.sh
        ```
        ### Windows:
        ```cmd
        start.bat
        ```

        ## COMANDOS MANUAIS
        1. Configurar Ambiente:
        ```bash
        python -m venv venv
        source venv/bin/activate  # Linux/Mac
        venv\Scripts\activate     # Windows
        pip install -r requirements.txt
        ```
        2. Testar Chave Gemini:
        ```bash
        python test_key.py
        ```
        3. Iniciar Serviço:
        ```bash
        python app.py
        ```
        4. Testes Completos:
        ```bash
        python test_service.py
        ```

        ## ENDPOINTS
        - GET / - Página inicial
        - GET /health - Health check
        - GET /api/status - Status do serviço
        - POST /api/recommend - Gerar recomendações
        - GET /api/test - Teste automático

        ## Segurança
        Nunca compartilhe sua GEMINI_API_KEY em repositórios públicos.
