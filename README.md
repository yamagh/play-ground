# Play Framework with Svelte Template

これは [Play Framework](https://www.playframework.com/) と [Svelte](https://svelte.dev/) を組み合わせた、実践的なWebアプリケーションのテンプレートです。

## 動機と目的

このプロジェクトは、Play Framework と Svelte というモダンな技術スタックを組み合わせた開発の出発点を提供することを目的としています。
昨今、多くのWebフレームワークはフロントエンドとバックエンドが分離したSPA（Single Page Application）構成を前提としていますが、一方で、伝統的なサーバサイドレンダリングの良さも見直されています。

このテンプレートは、Play Framework の強力なサーバサイド機能と、Svelte のリアクティブで高速なUI構築能力を統合し、両者の利点を活かした開発体験を提供します。

具体的には、以下のような基本的な機能のリファレンスを提供します。

-   **ユーザー認証**: ログイン・ログアウト機能
-   **CRUD操作**: データベース（H2）と連携したタスクの検索、一覧表示、作成、更新機能
-   **ページネーション**: 大量データをページに分割して表示する機能
-   **REST API**: フロントエンドから非同期にデータを操作するためのAPI

## 技術スタック

このプロジェクトで採用している主な技術は以下の通りです。

-   **バックエンド**
    -   [Play Framework 3 (Java)](https://www.playframework.com/)
    -   [Scala 2.13](https://www.scala-lang.org/) (ビルド定義)
    -   [sbt](https://www.scala-sbt.org/): ビルドツール
    -   [Ebean](https://ebean.io/): ORMフレームワーク
    -   [H2 Database](https://www.h2database.com/): インメモリデータベース

-   **フロントエンド**
    -   [Svelte](https://svelte.dev/)
    -   [TypeScript](https://www.typescriptlang.org/)
    -   [Sveltestrap](https://sveltestrap.github.io/): Bootstrap 5 コンポーネント
    -   [Webpack](https://webpack.js.org/): モジュールバンドラ

-   **統合**
    -   [sbt-web](https://github.com/sbt/sbt-web): Play Framework とフロントエンドビルドの統合
    -   [sbt-svelte](https://github.com/tanin47/sbt-svelte): sbt による Svelte のコンパイル

## アーキテクチャ

-   **サーバサイドMVC**: Play Framework の標準的なMVCアーキテクチャに準拠しています。
    -   `app/controllers`: HTTPリクエストを処理します。
    -   `app/models`: Ebean を利用したデータモデルを定義します。
    -   `app/views`: Twirl テンプレートエンジン（`.scala.html`）を使用してHTMLをレンダリングします。
-   **フロントエンドコンポーネント**:
    -   `app/assets/svelte`: Svelteコンポーネントのソースコードが配置されています。
    -   `screen.scala.html` と `loadSvelteAssets.scala.html` を通じて、サーバサイドでレンダリングされたHTMLページにSvelteコンポーネントがマウントされます。
-   **API**:
    -   `conf/routes` で定義された `/api` パス以下のエンドポイントは、JSONを返すRESTful APIとして機能します。

## オンボーディング (Getting Started)

### 前提条件

-   [JDK 17](https://adoptium.net/) 以降
-   [sbt](https://www.scala-sbt.org/download.html) 1.5.0 以降
-   [Node.js](https://nodejs.org/) 14.x 以降
-   [npm](https://www.npmjs.com/) 7.x 以降

### インストールと実行

1.  **リポジトリをクローンします**
    ```bash
    git clone https://github.com/yamagh/play-ground.git
    cd play-ground
    ```

2.  **npm パッケージをインストールします**
    ```bash
    npm install
    ```

3.  **Play Framework アプリケーションを実行します**
    ```bash
    sbt run
    ```
    アプリケーションは `http://localhost:9000` で起動します。
    データベースはH2インメモリデータベースを使用しており、アプリケーションの起動時にEvolutionsによってテーブルが自動的に作成されます。

### 開発中のホットリロード

フロントエンドの変更を即座に反映させるために、HMR（Hot Module Replacement）を利用できます。

1.  `sbt run` でバックエンドサーバを起動したまま、別のターミナルを開きます。
2.  以下のコマンドでHMRサーバを起動します。
    ```bash
    npm run hmr
    ```

これにより、SvelteコンポーネントやCSSの変更がブラウザをリロードすることなく適用されます。

## ディレクトリ構成

- `app` - アプリケーションのソースコード
  - `assets` - フロントエンドのアセット (Svelte, CSS, etc.)
    - `svelte/components` - 共通のUIコンポーネント (フォーム、ページネーションなど)
    - `svelte/layouts` - アプリケーション全体のレイアウト
    - `svelte/screens` - 各画面に対応するSvelteコンポーネント (ログイン画面、タスク一覧画面など)
    - `svelte/stores` - Svelteのストア (状態管理)
    - `svelte/utils` - ユーティリティ関数 (APIクライアントなど)
  - `controllers` - アプリケーションのコントローラー (Scala/Java)
    - `actions` - 認証チェックなどのカスタムアクション
    - `api` - JSONを返すAPI用のコントローラー
    - `web` - HTMLをレンダリングするWebページ用のコントローラー
  - `models` - アプリケーションのビジネスロジック (モデル)
  - `repository` - データベースアクセス層
  - `services` - サービス層
  - `views` - テンプレート (主に `scala.html`)
- `build.sbt` - sbt のビルドスクリプト
- `conf` - 設定ファイル
  - `application.conf` - メインの設定ファイル
  - `routes` - ルーティング定義
  - `evolutions` - データベースのマイグレーションスクリプト
- `public` - 公開される静的アセット
- `test` - テストコード
- `package.json` - Node.js の依存関係とスクリプト
- `webpack.config.js` - Webpack の設定ファイル
- `tsconfig.json` - TypeScript の設定ファイル

## ライセンス

このプロジェクトは [MIT License](LICENSE) の下で公開されています。
