# Sobre os Dados de Empresas Inativas

Este documento descreve o formato e as características dos dados relacionados às empresas inativas, incluindo os tipos, organização e limitações encontradas ao trabalhar com essas informações.

## Tipos de Empresas Inativas

Os dados de empresas inativas são categorizados em três estados:

1. **Inaptas**
2. **Suspensas**
3. **Baixadas**

### Datas de Inatividade

- Apenas empresas do tipo **baixadas** possuem informações relacionadas à **data de inatividade**.
- Para os tipos **inaptas** e **suspensas**, a única data disponível nos dados é a **data de ativação/criação da empresa**.

Essa limitação apresenta um desafio na organização de empresas inativas, pois não faz sentido classificá-las pela data de ativação quando o objetivo é refletir sua situação atual.

## Organização dos Dados

Os dados são organizados de forma padronizada por **ano**, alinhados com um esquema geral de estruturação de dados. Este padrão foi escolhido para facilitar a integração e análise dos dados ao longo do tempo.

### Racionalidade na Organização

Embora as datas de ativação não sejam adequadas para as empresas inativas dos tipos **inaptas** e **suspensas**, manter uma estrutura consistente de organização é essencial para preservar o padrão estabelecido no esquema de dados.

## Conclusão

Este esquema padronizado permite gerenciar os dados de forma uniforme, mesmo com as limitações associadas à falta de datas de inatividade para algumas categorias de empresas inativas. Caso seja necessário realizar uma análise mais precisa das empresas inativas, recomenda-se explorar métodos adicionais para inferir ou imputar datas relevantes a partir de outras variáveis disponíveis.
