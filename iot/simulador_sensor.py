"""
============================================================================
 Simulador IoT - "Tem Satelite Passando Por Mim Agora?"  (Global Solution FIAP)
============================================================================
 Simula um sensor de condicao do ceu instalado em uma localizacao e envia
 as leituras (temperatura, chuva, nebulosidade, luminosidade) para a API.

 A API aplica a regra academica de observacao:
   favoravel = NAO chove  E  nebulosidade < 40%  E  luminosidade < 50%

 USO:
   1) Suba a API (perfil dev):  ver GS/backend/README ou GS/README.md
   2) Instale a dependencia:    pip install requests
   3) Rode:                     python simulador_sensor.py
                                python simulador_sensor.py --once   (1 leitura)

 Sem a API no ar, o script ainda imprime o JSON gerado (modo mock).
============================================================================
"""

import argparse
import json
import random
import time
from datetime import datetime

try:
    import requests
except ImportError:
    requests = None  # funciona em modo "apenas imprime JSON"

API_URL = "http://localhost:8080/leituras"
LOCALIZACAO_ID = 1          # Niteroi - RJ (seed da API)
INTERVALO_SEGUNDOS = 5


def gerar_leitura(localizacao_id: int) -> dict:
    """Gera uma leitura simulada do sensor (valores aleatorios plausiveis)."""
    return {
        "localizacaoId": localizacao_id,
        "temperatura": random.randint(15, 32),       # graus Celsius
        "chovendo": random.random() < 0.3,           # 30% de chance de chuva
        "nebulosidade": random.randint(0, 100),      # % de nuvens
        "luminosidade": random.randint(0, 100),      # % de poluicao luminosa
    }


def avaliar_observacao(leitura: dict) -> bool:
    """Mesma regra usada na API e no app mobile."""
    return (
        not leitura["chovendo"]
        and leitura["nebulosidade"] < 40
        and leitura["luminosidade"] < 50
    )


def enviar(leitura: dict) -> None:
    """Envia a leitura para a API (POST /leituras) ou imprime em modo mock."""
    favoravel = avaliar_observacao(leitura)
    hora = datetime.now().strftime("%H:%M:%S")
    print(f"\n[{hora}] Leitura gerada:")
    print(json.dumps(leitura, indent=2, ensure_ascii=False))
    print(f"  -> Observacao favoravel (calculo local): {favoravel}")

    if requests is None:
        print("  [MOCK] biblioteca 'requests' nao instalada - nao enviei para a API.")
        return

    try:
        resp = requests.post(API_URL, json=leitura, timeout=3)
        if resp.status_code in (200, 201):
            corpo = resp.json()
            print(f"  [API] Salvo! id={corpo.get('id')} "
                  f"observacaoFavoravel={corpo.get('observacaoFavoravel')}")
        else:
            print(f"  [API] Resposta {resp.status_code}: {resp.text}")
    except Exception as e:  # API fora do ar
        print(f"  [MOCK] API indisponivel ({e}). Leitura nao enviada.")


def main():
    parser = argparse.ArgumentParser(description="Simulador IoT de condicao do ceu")
    parser.add_argument("--once", action="store_true", help="envia apenas uma leitura")
    parser.add_argument("--localizacao", type=int, default=LOCALIZACAO_ID,
                        help="id da localizacao (padrao: 1)")
    parser.add_argument("--intervalo", type=int, default=INTERVALO_SEGUNDOS,
                        help="segundos entre leituras (padrao: 5)")
    args = parser.parse_args()

    print("==============================================")
    print(" Simulador IoT - condicao do ceu")
    print(f" Enviando para: {API_URL}")
    print(f" Localizacao id: {args.localizacao}")
    print("==============================================")

    if args.once:
        enviar(gerar_leitura(args.localizacao))
        return

    try:
        while True:
            enviar(gerar_leitura(args.localizacao))
            time.sleep(args.intervalo)
    except KeyboardInterrupt:
        print("\nSimulador encerrado.")


if __name__ == "__main__":
    main()
