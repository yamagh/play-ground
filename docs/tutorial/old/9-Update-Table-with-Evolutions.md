# 4-1. Play FrameworkのEvolutionsでテーブルを更新する

これまではダミーデータを使ってきましたが、この章からは実際にデータベースにデータを保存・更新する処理を実装していきます。
まずはじめに、お知らせ情報を格納するためのテーブルをデータベースに作成します。

Play Frameworkには **Evolutions** というデータベースマイグレーションのための仕組みが標準で備わっています。
Evolutionsは、SQLスクリプトを使ってデータベースのスキーマ（テーブル構造など）をバージョン管理するための機能です。

## 1. Evolutionスクリプトの作成

`conf/evolutions/default` ディレクトリに、新しいSQLファイルを作成します。
ファイル名は `3.sql` とします。ファイル名の数字は、Evolutionsが適用される順序を表します。

**`conf/evolutions/default/3.sql`**

```sql
# --- !Ups

-- お知らせテーブル
CREATE TABLE news (
  id          BIGINT AUTO_INCREMENT NOT NULL,
  news_date   DATE NOT NULL,
  title       VARCHAR(255) NOT NULL,
  CONSTRAINT pk_news PRIMARY KEY (id)
);

-- 初期データ
INSERT INTO news (news_date, title) VALUES
  ('2025-09-01', 'システムメンテナンスのお知らせ'),
  ('2025-08-15', '新機能のリリースについて'),
  ('2025-07-20', '夏季休業のお知らせ');


# --- !Downs

DROP TABLE IF EXISTS news;
```

### スクリプトの解説

- **`# --- !Ups`**
  このセクションに書かれたSQLは、マイグレーションを**適用**する（バージョンを上げる）時に実行されます。
  ここでは `news` テーブルの作成と、3件の初期データの挿入を行っています。
  - `id BIGINT AUTO_INCREMENT NOT NULL`: 主キーとなるID。自動で連番が振られます。
  - `news_date DATE NOT NULL`: お知らせの日付。
  - `title VARCHAR(255) NOT NULL`: お知らせのタイトル。

- **`# --- !Downs`**
  このセクションに書かれたSQLは、マイグレーションを**元に戻す**（バージョンを下げる）時に実行されます。
  `!Ups` セクションで行った変更を完全に取り消すためのSQLを記述する必要があります。
  ここでは `news` テーブルを削除しています。

## 2. Evolutionの適用

Play Frameworkを開発モード (`sbt run`) で実行している場合、Evolutionsスクリプトに新しいバージョンが追加されると、次回のブラウザリクエスト時に自動的に検知されます。

1.  ブラウザで [http://localhost:9000](http://localhost:9000) のいずれかのページにアクセスします。
2.  **"Database 'default' needs evolution!"** というメッセージが表示された画面に遷移します。
3.  これは、Playが `3.sql` という新しいスクリプトを発見し、「データベースのスキーマが古い状態なので、更新が必要です」と教えてくれている状態です。
4.  表示されているSQLスクリプト (`CREATE TABLE news ...`) が意図したものであることを確認し、**"Apply this script now!"** ボタンをクリックします。

ボタンをクリックすると、`!Ups` セクションのSQLが実行され、データベースに `news` テーブルが作成されます。
その後、元のページにリダイレクトされます。

もしエラーが発生した場合は、"Apply this script now!" ボタンの隣にある "Manually apply" の手順に従って手動で修正することもできます。

---

これで、データベース側にお知らせ情報を格納する準備が整いました。
次のコラムでは、この作成されたテーブルを実際に確認する方法として、H2データベースコンソールへのアクセス方法を学びます。
