# Gemini.md

このファイルは、このリポジトリで作業するAIコーディングエージェントへの指示を提供します。これは [AGENTS.md](https://agents.md) フォーマットに基づいています。

## プロジェクト概要

このプロジェクトは、バックエンドにPlay Framework（ScalaとJavaを使用）、フロントエンドにSvelteを使用して構築されたWebアプリケーションです。タスク管理アプリケーションのようです。

## はじめ方

### 前提条件

- Java Development Kit (JDK)
- sbt (The Scala Build Tool)
- Node.js と npm

### ビルドと実行

1.  **フロントエンドの依存関係をインストールします:**
    ```bash
    npm install
    ```
2.  **アプリケーションを実行します（バックエンドとフロントエンド）:**
    ```bash
    sbt run
    ```

アプリケーションは `http://localhost:9000` で利用可能になります。Play Frameworkがバックエンドを処理し、`webpack`（`webpack.config.js`で設定）がSvelteのフロントエンドアセットをコンパイルして提供します。

## テスト

テストを実行するには、次のコマンドを使用します:

```bash
sbt test
```

ブラウザテストは `test/browsers/` ディレクトリにあります。

## コードスタイル

- **バックエンド (Scala/Java):** 標準的なPlay Frameworkの規約に従ってください。
- **フロントエンド (Svelte/TypeScript):** このプロジェクトではTypeScript (`tsconfig.json`) を使用しています。既存のコーディングスタイルに従ってください。

## 主要なファイルの場所

- **バックエンドコントローラー:** `app/controllers/`
- **APIコントローラー:** `app/controllers/api/`
- **バックエンドモデル:** `app/models/`
- **データベースエボリューション:** `conf/evolutions/default/`
- **ルーティング:** `conf/routes`
- **Svelteコンポーネント:** `app/assets/svelte/`
- **メインSvelte画面:** `app/assets/svelte/screens/`

## セキュリティに関する考慮事項

- 一般的なWebアプリケーションの脆弱性（例：XSS、CSRF）に注意してください。
- 特にコントローラーでユーザー入力をサニタイズしてください。
- パラメータ化されたクエリを使用してSQLインジェクションを防ぎます（このプロジェクトではEbean ORMを使用しており、これが役立ちます）。

## コミットメッセージ

明確で理解しやすいプロジェクト履歴を維持するために、従来のコミットメッセージ形式に従ってください。