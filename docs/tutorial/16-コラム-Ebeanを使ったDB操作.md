# 4-3. コラム: Ebeanを使ったDB操作

データ取得APIの実装で `Ebean` というライブラリを使用しました。Ebeanは、Play Frameworkに標準で組み込まれているORM（Object-Relational Mapping）の一つです。この章では、Ebeanがどのようなもので、どのようにDB操作を簡単にしてくれるのかを解説します。

## Ebeanとは

ORMとは、その名の通り、プログラミング言語の**オブジェクト**と、リレーショナルデータベースのテーブルやレコードを**対応付ける（マッピングする）**ための技術やライブラリのことです。

ORMを使わない場合、データベースを操作するにはSQL文を文字列として組み立て、JDBCのような低レベルなAPIを使って実行し、返ってきた結果セット（ResultSet）から手動で値を取り出してオブジェクトに詰め替える、といった煩雑なコードを書く必要がありました。

EbeanのようなORMを使うと、以下のように多くのメリットがあります。
- **直感的な操作**: SQLを直接書かなくても、`save()` や `find.byId()` といったメソッドを呼び出すだけで、オブジェクトをそのままデータベースに保存したり、データベースから取得したデータをオブジェクトとして直接受け取ったりできます。
- **生産性の向上**: 定型的なCRUD（作成、読み取り、更新、削除）操作のコードを大幅に削減できます。
- **静的型付けの恩恵**: Javaのような静的型付け言語のメリットを活かし、テーブル名やカラム名を文字列としてハードコーディングする箇所を減らすことで、コンパイル時にエラーを発見しやすくなります（型安全）。
- **データベースへの依存性の低減**: EbeanがデータベースごとのSQL方言の違いを吸収してくれるため、コードの変更を最小限に抑えつつ、開発環境ではH2、本番環境ではPostgreSQLといったようにデータベースを切り替えることが比較的容易になります。

## 基本的なCRUD操作

Ebeanでは、`@Entity` アノテーションが付いたモデルクラス（このプロジェクトでは `BaseModel` を継承）を通じて、基本的なCRUD操作を簡単に行うことができます。

```java
// 新しいTaskオブジェクトを作成
Task newTask = new Task();
newTask.title = "Ebeanの学習";

// --- Create (作成) ---
newTask.save(); // データベースに新しいレコードとして挿入

// --- Read (読み取り) ---
// 主キーで1件取得
Task foundTask = Task.find.byId(newTask.id);

// 全件取得
List<Task> allTasks = Task.find.all();

// --- Update (更新) ---
if (foundTask != null) {
    foundTask.isDone = true;
    foundTask.update(); // 変更をデータベースに反映
}

// --- Delete (削除) ---
if (foundTask != null) {
    foundTask.delete(); // データベースからレコードを削除
}
```

## Finderの活用

`Task` モデルに定義した `public static final Finder<Long, Task> find = new Finder<>(Task.class);` という一行が、Ebeanの強力なクエリビルディング機能の入り口となります。

この `find` オブジェクトを使うことで、SQLライクなメソッドチェーンで、より複雑なデータ取得の条件を型安全に記述できます。

```java
// --- 条件を指定して検索 ---
// isDoneがfalseのタスクを検索
List<Task> incompleteTasks = Task.find.query()
    .where()
    .eq("isDone", false)
    .findList();

// --- 並び替え ---
// createdAt（作成日時）の降順（新しい順）で並び替え
List<Task> sortedTasks = Task.find.query()
    .orderBy("createdAt desc")
    .findList();

// --- ページネーション ---
// 最初の10件をスキップして、次の10件を取得（2ページ目）
List<Task> pagedTasks = Task.find.query()
    .setFirstRow(10) // 開始位置 (0-indexed)
    .setMaxRows(10)  // 取得件数
    .findList();

// --- 組み合わせ ---
// isDoneがfalseのタスクを、更新日時の新しい順で、最初の5件取得
List<Task> complexQueryTasks = Task.find.query()
    .where()
    .eq("isDone", false)
    .orderBy("updatedAt desc")
    .setMaxRows(5)
    .findList();
```

このように、EbeanはSQLの知識を活かしつつ、Javaのオブジェクトとして直感的にデータベースを操作できる強力なツールです。これにより、開発者はSQLインジェクションのようなセキュリティリスクを低減し、ビジネスロジックの実装に集中することができます。
