# コラム: EBeanを使ったDB操作

このチュートリアルでは、データベースへのアクセスに **Ebean** というORMライブラリを使用しました。
ORMは Object-Relational Mapping（オブジェクト関係マッピング）の略で、Javaのオブジェクトとリレーショナルデータベースのテーブルを対応付け、SQLを直接書かなくてもデータベース操作を可能にする技術です。

## Ebeanの主な特徴

### 1. シンプルなAPI
Ebeanは、直感的で使いやすいAPIを提供します。

- **検索 (Find):**
  ```java
  // IDで一件取得
  Task task = Task.find.byId(1L);

  // 条件を指定してリスト取得
  List<Task> incompleteTasks = Task.find.query()
      .where()
      .eq("done", false)
      .orderBy("dueDate asc")
      .findList();
  ```

- **保存 (Save):**
  ```java
  Task task = new Task();
  task.title = "新しいタスク";
  task.save(); // INSERT or UPDATE を自動で判断
  ```

- **削除 (Delete):**
  ```java
  Task task = Task.find.byId(1L);
  if (task != null) {
    task.delete();
  }
  ```
  `NewsRepository` で `ebeanServer.find()` のように明示的に `EbeanServer` を使ったのは、非同期処理のスレッドを適切に管理するためですが、`BaseModel` を継承したモデルでは上記のようなよりシンプルな静的メソッド風のAPIも利用できます。

### 2. クエリ生成
開発者はオブジェクト指向の考え方でメソッドをチェーンさせてクエリを組み立てることができ、Ebeanが裏側で適切なSQLを生成してくれます。これにより、タイプミスによるSQLのシンタックスエラーなどを減らすことができます。

### 3. SQLも書ける
ORMは便利ですが、複雑なクエリやパフォーマンスチューニングが必要な場面では、生のSQLを書きたいこともあります。Ebeanはそうしたケースにも対応しています。

- **Raw SQL:**
  ```java
  String sql = "SELECT id, title FROM task WHERE done = :done";
  List<SqlRow> rows = Ebean.createSqlQuery(sql)
      .setParameter("done", false)
      .findList();
  ```

- **Dto (Data Transfer Object) へのマッピング:**
  SQLの実行結果を、モデルオブジェクトではなく、特定の目的に特化したDTOに直接マッピングすることもでき、効率的なデータ取得が可能です。

## なぜRepository層を設けるのか？

Controllerから直接 `Ebean` のAPIを呼び出すことも技術的には可能です。しかし、このリポジトリでは `NewsRepository` のような**Repository層**を間に挟んでいます。これにはいくつかの理由があります。

- **関心の分離 (Separation of Concerns):**
  - **Controller** は「HTTPリクエストを受け取り、レスポンスを返す」ことに関心を持ちます。
  - **Repository** は「データベースとのやり取り」に関心を持ちます。
  - このように役割を分離することで、コードの見通しが良くなり、各クラスが単一の責任を持つようになります。

- **テストのしやすさ:**
  データベースアクセスロジックがRepositoryに集約されているため、テストが容易になります。Controllerをテストする際に、本物のデータベースに接続する代わりに、Repositoryのモック（偽物）を注入することで、Controllerのロジックだけを独立してテストできます。

- **再利用性:**
  同じデータベースクエリ（例えば「全ての未完了タスクを取得する」）を複数のControllerやServiceから利用したい場合、Repositoryにそのメソッドを定義しておけば、ロジックを重複させることなく再利用できます。

- **トランザクション管理:**
  複数のDB操作を一つのトランザクションとしてまとめたい場合など、トランザクションの境界をRepository層で管理することで、ビジネスロジックをより明確に記述できます。

EbeanのようなORMと、Repositoryパターンのような設計原則を組み合わせることで、生産性と保守性の高いデータアクセス層を構築することができます。
