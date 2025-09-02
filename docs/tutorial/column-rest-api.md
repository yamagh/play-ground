# コラム: REST API設計の基本

前の章では、お知らせ一覧を取得するためのAPI `/api/news` を作成しました。
このように、サーバー上の情報（リソース）を、URLを通じて、HTTPメソッド（GET, POST, PUT, DELETEなど）を使って操作できるように設計されたAPIのことを **RESTful API** と呼びます。

REST (REpresentational State Transfer) は、Webのアーキテクチャスタイルの一つであり、その原則に従って設計されたAPIは、シンプルで一貫性があり、理解しやすいという特徴を持ちます。

## RESTの基本原則

### 1. リソース (Resource)
APIが操作する対象はすべて「リソース」として表現されます。リソースはURL（Uniform Resource Locator）によって一意に識別されます。
- 例: `/tasks` (タスクのコレクション), `/tasks/123` (IDが123の特定のタスク), `/users/yamada/profile` (山田さんのプロフィール)

URLには、リソースを指し示す**名詞**を使うのが一般的です。「タスクを取得する」という意味の `/getTasks` のような**動詞**は通常使いません。

### 2. HTTPメソッド (Verbs)
リソースに対する操作は、HTTPメソッド（動詞）を使って表現します。

| HTTPメソッド | CRUD操作 | 説明                                       |
| :----------- | :------- | :----------------------------------------- |
| **GET**      | **Read**   | リソースを取得する (例: `/tasks` で一覧取得) |
| **POST**     | **Create** | 新しいリソースを作成する (例: `/tasks` にデータを送信して新規作成) |
| **PUT**      | **Update** | 既存のリソースを更新・置換する (例: `/tasks/123` を新しいデータで上書き) |
| **PATCH**    | **Update** | 既存のリソースを部分的に更新する (例: `/tasks/123` のタイトルだけ変更) |
| **DELETE**   | **Delete** | リソースを削除する (例: `/tasks/123` を削除) |

このように、「何を (`/tasks/123`)」「どうする (`DELETE`)」がURLとHTTPメソッドで明確に分離されるのがRESTの美しい点です。

### 3. 表現 (Representation)
クライアントとサーバーは、リソースの「表現」をやり取りします。この「表現」のフォーマットとして、現在最も一般的に使われているのが **JSON (JavaScript Object Notation)** です。
サーバーはデータベースから取得したタスクの情報をJSON形式に変換してクライアントに返し、クライアントはユーザーが入力した新しいタスクの情報をJSON形式でサーバーに送信します。

### 4. ステートレス (Stateless)
サーバーは、クライアントの状態を一切保持しません。各リクエストは、それ自体で完結するために必要な情報をすべて含んでいる必要があります。
例えば、認証情報（「誰が」リクエストしているか）は、リクエストごとにHTTPヘッダーなどに含めて送信する必要があります。これにより、サーバーの実装がシンプルになり、スケーラビリティが向上します。

## このリポジトリでの例

`conf/routes` を見ると、この原則に沿ってAPIが設計されていることがわかります。

```
# タスクリソース (/api/tasks)
GET     /api/tasks                  controllers.api.TaskController.list    # 一覧取得 (Read)
POST    /api/tasks                  controllers.api.TaskController.add     # 新規作成 (Create)
GET     /api/tasks/:id              controllers.api.TaskController.get(id: Long)     # 個別取得 (Read)
PUT     /api/tasks/:id              controllers.api.TaskController.update(id: Long)  # 更新 (Update)
DELETE  /api/tasks/:id              controllers.api.TaskController.delete(id: Long)  # 削除 (Delete)
```

一貫したルールでAPIを設計することで、APIの利用者は次にどのようなURLやメソッドを使えば良いかを推測しやすくなり、開発効率が向上します。
