# 31. 付録: Play Framework開発Tips

Play Frameworkでの開発をより効率的に進めるための、いくつかのヒントとテクニックを紹介します。

## 1. `~run` による自動リロード

チュートリアルでは `sbt run` コマンドを使いました。これは開発モードでサーバーを起動し、リクエストを受け取るたびにソースコードの変更を検知して自動的にリコンパイルしてくれる便利な機能です。

しかし、`build.sbt` や `plugins.sbt` のようなビルド定義ファイルや、`conf/routes` 以外の `conf` ディレクトリ内のファイルを変更した場合、`sbt run` は自動ではリロードしてくれません。

このような場合、`sbt` のチルダ (`~`) プレフィックスが役立ちます。

```bash
sbt ~run
```

`~run` を使ってサーバーを起動すると、sbtは `.scala` や `.java` ファイルだけでなく、`.sbt` ファイルや `conf` ディレクトリ内のファイルの変更も監視するようになります。
これらのファイルを変更・保存すると、sbtは自動的にビルド全体をリロードし、サーバーを再起動します。
これにより、手動でsbtを再起動する手間が省け、開発サイクルをさらに高速化できます。

## 2. デバッグとロギング

バックエンドの処理が期待通りに動かない場合、変数の中身や処理の通過点を確認したくなります。

### ロギング
最も手軽なデバッグ方法は、ログ出力です。Play Frameworkでは、SLF4J (Simple Logging Facade for Java) を使って簡単のログを出力できます。

```java
package controllers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// ...

public class TaskController extends Controller {
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    public Result list(Http.Request request) {
        final String term = request.getQueryString("term");
        log.info("Searching tasks with term: {}", term); // ログ出力

        final PagedList<Task> tasks = this.taskRepository.find(term);
        // ...
        return ok(Json.toJson(tasks));
    }
}
```
- `LoggerFactory.getLogger(...)` でロガーインスタンスを取得します。
- `log.info("...", term)` のようにして、変数を含むメッセージを簡単に出力できます。
- ログレベルには `trace`, `debug`, `info`, `warn`, `error` があり、`conf/logback.xml` で出力レベルを調整できます。
- 出力されたログは、`sbt run` を実行しているターミナルに表示されます。

### デバッガの利用
より高度なデバッグには、IDEのデバッガを利用するのが効果的です。
`sbt` をデバッグモードで起動するには、`sbt -jvm-debug 5005` のようにポートを指定して起動し、IDEからリモートデバッグ接続します。
`launch.json` が設定済みのVSCodeなどでは、デバッグ実行を開始するだけで、ブレークポイントを設定してステップ実行や変数のインスペクションが可能になります。

## 3. Playのエラーページを読み解く

開発モードのPlayは、エラーが発生した際に非常に詳細な情報をブラウザに表示してくれます。このエラーページを正しく読み解くことが、問題解決の鍵となります。

- **Compilation Error**:
  ソースコードに文法的な誤りがある場合に表示されます。エラーメッセージには、問題のあるファイル名、行番号、そして「`;` がありません」や「シンボルが見つかりません」といった具体的なエラー内容が示されます。まずはメッセージをよく読み、指摘された箇所を確認しましょう。

- **Routing Error**:
  `conf/routes` に定義されていないURLにアクセスした場合や、Controllerのアクションの引数と `routes` の定義が一致しない場合に発生します。
  「Action Not Found」や「No route to ...」といったメッセージが表示されたら、`conf/routes` の記述が正しいか、HTTPメソッド（`GET`, `POST`など）は合っているかを確認しましょう。

- **NullPointerException などの実行時例外**:
  コードの実行中に発生した例外です。エラーページには、例外が発生したクラス、メソッド、行番号を示す「スタックトレース」が表示されます。
  スタックトレースは下から上に読んでいくのが基本です。自分の書いたコード（`controllers.` や `models.` などで始まる行）がどこにあるかを探し、その周辺で `null` になる可能性のある変数がないかなどを確認します。

これらのTipsを活用して、快適なPlay Framework開発ライフをお過ごしください。
