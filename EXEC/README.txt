【利用手順】
 1. タスクスケジューラにStockStatus_win.jarを定期実行するように設定する
 　実行コマンド：java -jar (EXECを置いたフォルダ)/EXEC/StockStatus_win.jar
 2. EXEC/env.properties（入力ファイル）に設定を書く
 -以上-


【各種フォルダとファイル】
■env.properties（入力ファイル）
　設定情報が書かれるファイル
　※以下、「◯」は要ユーザ設定、「△」はユーザ拡張可、「×」はデフォルト設定推奨

　×　1. userName：通知メールの送信元アカウントのユーザ名
　×　2. password：1.のパスワード
　◯  3. sendTo　：通知メールの送信先アドレス
　△　4. pageNum ：検知対象サイトのURL数
　△  5. pageURL0：検知対象サイト1
　△  6. pageURL1：検知対象サイト2
 　  .
　   .
　△  X. pageURLX：検知対象サイトX

■output
　PGMが取ってきたリソース（HTML,GIF）が置かれるフォルダ。
　　・各店舗の在庫情報ページHTMLファイル
　　・在庫状況GIFファイル

■StockStatus.jar
　コレがPGM本体

■StockStatus_lib
　本体をエラー無しで実行させるために必要な外部ライブラリ


【PGMがいよう】
■内部で何をしている？
　1. 入力ファイルから処理に必要なデータをロードする
　2. 設定された在庫状況ページURLにアクセスしてHTMLファイルを取ってくる
　3. HTMLファイルを解析して、在庫状況GIFファイルが置かれたURLを確認する
　4. GIFファイルが置かれたURLにアクセスしてGIFファイル取ってくる
　5. GIFファイル内で特定のX座標Y座標が何色か取得して在庫有無を判断する
　6. 在庫があれば「店舗＋商品仕様」をメール本文に載せて送る
　7. 在庫がなければ「No news」をメール本文に載せて送る

authored by takashi kato
in Oct.25 2014
