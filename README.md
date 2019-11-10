# オセロ

## 概要
- CUIで動作し、4種のゲームモードを利用可能
  - プレイヤー VS プレイヤー
  - 対AI(弱)
  - 対AI(強)
  - AI(弱) VS AI(強)

- 敵AIは以下  
  - ランダムにコマを置くAI
  - アルファベータ法により評価値を計算しコマを置くAI
  
- AI同士の対戦を見ることができる

## 使用したイディオム・パターン
- staticファクトリメソッド
- FlyWeightパターン(本来のそれとは少々異なる)
- Strategyパターン
- 列挙型クラスの活用

## 各ファイルについて
- Main.java: mainメソッドを含むゲーム進行を担当するメインクラス

- Field.java: オセロのフィールドの保持を主に担当するクラス
- Piece.java: コマの状態の保持を担当するクラス
- Coordinate.java: 座標を表すクラス

- Vector.java: 方向を表す列挙型クラス
- PieceType.java: コマの状態の列挙型クラス

- GameMode.java: 遊ぶことができるゲームのモードを管理する列挙型クラス
- Player.java: AIでなく、実際のプレイヤーの場合に入力を求める戦略パターンの1つ
- WeakAI.java: ランダムにコマを置くAI.
- StrongAi.java: アルファベータ法によりコマを置くAI.
- Strategy.java: 2種類のコマそれぞれに対する戦略を保持するクラス.
- StrategyInterface.java: 戦略クラスのポリフォーリズムを可能にするインターフェース.