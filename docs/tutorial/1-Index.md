# Play Framework & Svelte MPAチュートリアル

このチュートリアルでは、Play FrameworkとSvelteを使用してマルチページアプリケーション（MPA）を開発するための基本的な手順を学びます。
静的なページの作成から始め、データの表示、そしてデータの更新処理まで、段階的にアプリケーションを構築していきます。

## 前提事項

このチュートリアルは、以下の環境を前提としています。

- **OS:** Windows
- **ターミナル:** コマンドの実行にはPowerShellまたはコマンドプロンプトを使用します。

## 目次

### 1. イントロダクション
- **1-1. [このリポジトリのアーキテクチャ概要](./2-Architecture.md)**
- **1-2. [開発環境のセットアップ](./3-Setup.md)**
- **コラム: [Play Frameworkとは](./column-play-framework.md)**
- **コラム: [Svelteとは](./column-svelte.md)**
- **コラム: [SveltestrapによるUIデザイン](./column-sveltestrap.md)**
- **コラム: [BootstrapによるUIデザイン](./column-bootstrap.md)**

### 2. 静的なページの作成
- **2-1. [Svelteコンポーネントの作成](./4-Create-Svelte-Component.md)**
- **2-2. [Play FrameworkのControllerを作成する](./5-Create-Play-Controller.md)**
- **2-3. [標準レイアウトコンポーネントの利用](./6-Use-Standard-Layout.md)**
- **コラム: [sbtとwebpackによるビルドの仕組み](./column-build-system.md)**
- **コラム: [画面部品のコンポーネント化](./column-component-architecture.md)**

### 3. データの表示
- **3-1. [APIを作成してデータを取得する](./7-Create-API-for-Read.md)**
- **3-2. [SvelteコンポーネントからAPIを呼び出す](./8-Call-API-from-Svelte.md)**
- **コラム: [REST API設計の基本](./column-rest-api.md)**
- **コラム: [非同期処理とPromise](./column-async-promise.md)**

### 4. データの更新
- **4-1. [Play FrameworkのEvolutionsでテーブルを更新する](./9-Update-Table-with-Evolutions.md)**
- **コラム: [H2データベースとコンソール](./column-h2-database.md)**
- **4-2. [データを更新するAPIの作成](./10-Create-API-for-Update.md)**
- **4-3. [Svelteで更新フォームを作成する](./11-Create-Update-Form.md)**
- **コラム: [データベースマイグレーションの重要性](./column-db-migration.md)**
- **コラム: [EBeanを使ったDB操作](./column-ebean.md)**

### 5. まとめ
- **5-1. [学習の振り返りと次のステップ](./12-Conclusion.md)**

### 付録
- **A-1. [Svelte Runesを使ったデバッグ](./appendix-svelte-runes.md)**
- **A-2. [Play Framework開発Tips](./appendix-play-framework-tips.md)**
