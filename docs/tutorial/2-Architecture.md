# 1-1. このリポジトリのアーキテクチャ概要

この章では、アプリケーション全体の技術スタックとディレクトリ構成、そしてリクエストがどのように処理されるかの流れを解説します。

## 技術スタック

このリポジトリは、以下の主要な技術で構成されています。

- **バックエンド:** [Play Framework](https://www.playframework.com/) (Java/Scala)
  - Webフレームワークとして、HTTPリクエストの処理、HTMLのレンダリング、APIの提供など、サーバーサイドのロジック全般を担当します。
- **フロントエンド:** [Svelte](https://svelte.dev/)
  - UIを構築するためのコンポーネントベースのJavaScriptフレームワークです。ユーザーが直接操作する画面の描画やインタラクションを担当します。
- **ビルドツール:**
  - **sbt:** Scala/Javaのビルドツール。サーバーサイドのコンパイルや依存関係の管理を行います。
  - **Webpack:** JavaScriptのモジュールバンドラ。SvelteコンポーネントやTypeScriptをブラウザで実行可能なJavaScriptファイルに変換します。
- **データベース:** H2 Database
  - 開発用のインメモリデータベースです。Play FrameworkのEvolutions機能によってテーブルスキーマが管理されます。

## ディレクトリ構成の概要

プロジェクトの主要なディレクトリとその役割は以下の通りです。

```
.
├── app/
│   ├── assets/svelte/  # Svelteのソースコード (フロントエンド)
│   ├── controllers/    # HTTPリクエストを処理するクラス (バックエンド)
│   ├── models/         # データベースのエンティティ (バックエンド)
│   ├── repository/     # データベースアクセス層 (バックエンド)
│   └── views/          # Scalaテンプレートファイル (バックエンド)
├── conf/
│   ├── application.conf # アプリケーションの設定ファイル
│   └── routes           # URLとControllerのアクションを紐付けるルーティングファイル
├── public/             # 画像やCSSなどの静的ファイル
├── build.sbt           # sbtのビルド定義ファイル
└── package.json        # Node.jsの依存関係定義ファイル
```

## リクエスト処理の流れ (MPA)

このアプリケーションはMPA（マルチページアプリケーション）として構成されています。ユーザーがブラウザでページにアクセスしてから画面が表示されるまでの流れは以下のようになります。

1.  **ブラウザ:** ユーザーがURLにアクセスします。
2.  **Play Framework (Routing):** `conf/routes` ファイルの定義に基づき、リクエストURLに対応する`app/controllers`内のControllerのアクションが呼び出されます。
3.  **Play Framework (Controller):**
    - 必要に応じて `app/repository` を通じてデータベースからデータを取得します。
    - `app/views` にあるScalaテンプレート (`.scala.html`) を使用してHTMLを生成します。このとき、SvelteコンポーネントをマウントするためのコンテナとなるHTMLも同時にレンダリングされます。
4.  **ブラウザ:** サーバーから返されたHTMLを受け取り、ページを表示します。
5.  **Svelte:**
    - HTMLに含まれる `<script>` タグによって、WebpackでビルドされたJavaScriptファイルが読み込まれます。
    - JavaScriptが実行され、指定されたHTML要素（例: `<div id="app">`）にSvelteコンポーネントがマウントされます。これにより、動的なUIが構築されます。

このように、Play FrameworkがHTMLの骨格を生成し、Svelteがその中の特定のパーツを動的なコンポーネントとして描画することで、MPAとして動作します。

---

次のセクションでは、このアプリケーションを実際に動かすための開発環境のセットアップを行います。
