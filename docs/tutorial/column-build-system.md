# コラム: sbtとwebpackによるビルドの仕組み

このリポジトリでは、バックエンド（Play Framework）とフロントエンド（Svelte）でそれぞれ異なるビルドツールが動いています。
このコラムでは、`sbt` と `webpack` がどのように連携して、最終的にブラウザで表示されるページを生成しているのかを解説します。

## 登場人物

- **sbt (Simple Build Tool)**
  - Scala/Javaの世界のビルドツール。
  - ソースコードのコンパイル、依存ライブラリの管理、アプリケーションの起動などを担当します。
  - `build.sbt` ファイルがその設定ファイルです。

- **npm (Node Package Manager)**
  - JavaScriptの世界のパッケージ管理ツール。
  - `webpack` などのビルドツールや、Svelteのライブラリなどをインストールします。
  - `package.json` ファイルがその設定ファイルです。

- **webpack**
  - JavaScriptのモジュールバンドラ。
  - `.svelte` や `.ts` ファイルを、ブラウザが解釈できるプレーンな `.js` ファイルに変換（トランスパイル＆バンドル）します。
  - `webpack.config.js` ファイルがその設定ファイルです。

## ビルドの流れ

開発サーバーを起動した時（`sbt run` と `npm run hmr`）、裏側では以下のような処理が行われています。

1.  **`sbt run` (バックエンド)**
    - `app` ディレクトリ内の `.java` や `.scala` ファイルをコンパイルします。
    - Play Frameworkを起動し、HTTPリクエストを待ち受けます（デフォルトは9000番ポート）。

2.  **`npm run hmr` (フロントエンド)**
    - `webpack` を開発モード（HMR付き）で起動します。
    - `app/assets/svelte/screens/**/index.js` をエントリーポイントとして、関連する `.svelte` や `.ts` ファイルを解析します。
    - 解析したファイルをブラウザで実行可能なJavaScriptファイルに変換し、`target/web/public/main/javascripts/screens` ディレクトリに出力します。
    - ソースコードの変更を監視し、変更があれば差分だけを再ビルドしてブラウザに通知します。

3.  **ブラウザからのリクエスト**
    - ユーザーが `http://localhost:9000/news` にアクセスします。
    - Play Frameworkの`NewsController`がリクエストを処理し、`screen.scala.html` テンプレートをレンダリングします。
    - `screen.scala.html` の中には、以下のような `loadSvelteAssets` という処理が記述されています。

      ```scala
      @util.loadSvelteAssets(screenId)
      ```

    - この `loadSvelteAssets` は、`screenId`（今回は "news"）を元に、**webpackが出力したJavaScriptファイルのパス**（例: `/assets/javascripts/screens/news.js`）を解決し、`<script>` タグをHTMLに埋め込みます。
    - Play Frameworkは、完成したHTMLをブラウザに返します。

4.  **ブラウザでの描画**
    - ブラウザはHTMLを受け取り、`<script src="/assets/javascripts/screens/news.js">` タグを読み込みます。
    - `news.js` が実行され、Svelteコンポーネントが `id="app"` の要素にマウントされ、UIが表示されます。

## なぜ2つのツールが必要なのか？

- **sbt** はJVM言語（Java/Scala）のエコシステムに最適化されています。
- **webpack** はJavaScriptのエコシステム（Svelte, TypeScript, etc.）に最適化されています。

それぞれの得意分野を活かすために、2つのツールを連携させています。Play Frameworkは `sbt-play-scalajs` のようなプラグインを通じてフロントエンドのビルドプロセスを統合することも可能ですが、このリポジトリでは、よりモダンで柔軟なフロントエンド開発環境を維持するために、`sbt` と `webpack` を独立して実行する構成を採用しています。
