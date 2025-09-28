# 16. コラム: Ebeanを使ったDB操作

Ebeanは、JavaアプリケーションのためのORM (Object-Relational Mapping) ライブラリです。
Play Frameworkと緊密に統合されており、データベース操作をJavaのオブジェクトとして直感的に扱えるようにしてくれます。

## ORMとは？

ORMは、**Object** (Javaのオブジェクト) と **Relational** (リレーショナルデータベースのテーブル) の間の不一致を吸収し、両者を対応付け（**Mapping**）するための技術です。

ORMがない場合、データベースを操作するにはSQL文字列を組み立て、JDBC APIを使って実行し、返ってきた結果セット (ResultSet) を手動でループしてJavaオブジェクトに詰め替える、といった定型的ながら煩雑なコードを大量に書く必要がありました。

ORMを使うと、このような処理をライブラリが肩代わりしてくれます。開発者は、SQLを意識することなく、Javaのオブジェクトを操作するような感覚でデータベースを扱うことができます。

## Ebeanの基本

### 1. モデルの定義
Ebeanでは、データベースのテーブルはJavaのクラス（エンティティ）として表現されます。
`app/models/Task.java` を見てみましょう。

```java
package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import io.ebean.Model;
// ...

@Entity // Ebeanのエンティティであることを示す
public class Task extends Model {

    @Id // 主キーであることを示す
    public Long id;

    public String subject;

    // ...
}
```
- `@Entity` アノテーションを付けることで、このクラスがデータベースのテーブル（この場合は `tasks` テーブル）に対応することを示します。
- `@Id` アノテーションは、そのフィールドがテーブルの主キーであることを示します。
- `Model` クラスを継承することで、`save()`, `update()`, `delete()` といった便利なメソッドが使えるようになります。

### 2. データの取得 (Find)
`DB` または `Model` クラスの `find` メソッドを使って、流れるようなインターフェース（Fluent Interface）でクエリを構築できます。

```java
// IDで一件取得
final Task task = DB.find(Task.class, 1L);

// 全件取得
final List<Task> tasks = DB.find(Task.class).findList();

// 条件を指定して検索
final List<Task> highPriorityTasks = DB.find(Task.class)
    .where()
    .eq("priority", "High") // priority = 'High'
    .orderBy("dueDate asc")
    .findList();
```

### 3. データの登録 (Save)
新しいオブジェクトを作成し、`save()` メソッドを呼び出すだけで、`INSERT` 文が実行されます。

```java
final Task newTask = new Task();
newTask.subject = "新しいタスク";
newTask.save(); // INSERT INTO tasks (...) VALUES (...);
```

### 4. データの更新 (Update)
既存のデータを取得してフィールドを変更し、`update()` メソッドを呼び出すと `UPDATE` 文が実行されます。

```java
final Task task = DB.find(Task.class, 1L);
if (task != null) {
    task.subject = "更新されたタスク";
    task.update(); // UPDATE tasks SET subject = ... WHERE id = 1;
}
```

### 5. データの削除 (Delete)
`delete()` メソッドを呼び出すと `DELETE` 文が実行されます。

```java
final Task task = DB.find(Task.class, 1L);
if (task != null) {
    task.delete(); // DELETE FROM tasks WHERE id = 1;
}
```

## Ebeanの利点

- **生産性の向上**: SQLを直接書く必要がなく、定型的なコードが減るため、ビジネスロジックの実装に集中できます。
- **型安全性**: 文字列としてSQLを組み立てる際に起こりがちな、タイプミスによる実行時エラーをコンパイル時にある程度防ぐことができます。
- **SQLインジェクション対策**: Ebeanは自動的にプリペアドステートメント（Prepared Statement）を使用するため、開発者が意識しなくてもSQLインジェクションの脆弱性を防ぐことができます。

Ebeanは非常に多機能ですが、まずはこれらの基本的なCRUD (Create, Read, Update, Delete) 操作を覚えることが、バックエンド開発の第一歩となります。
