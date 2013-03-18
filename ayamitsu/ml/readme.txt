【MOD名】GoreMod-1.5.0-1.0.3 for ML

【概要】
EvilMinecraftの血痕の要素、BloodModの出血を併用したいがために作ったMODです。
mobに剣やツールで攻撃したら出血します。

既存書き換えがあるのでご注意を。

【前提MOD】
・ModLoader


【導入手順】
1.前提MODをminecraft.jarに導入、META-INFは消しておくこと
2.このMODをminecraft.jarに導入(他に直接導入するMODがあればそちらを先に導入すること)
3.完了


【コンフィグ】
・bloodRed(通常の血)
　赤い血の色の設定です

・bloodBlue
　青い血の設定です(蜘蛛さんの血)

・bloodBlack
　黒い血、というかイカ墨の色の設定です(イカのイカ墨)

・nearDamage
　近接攻撃でのダメージの種類です、MODで新たにダメージが追加された場合は追加するなどしていただければ(ただし、追加されたダメージが明記されることはなかなかないのでクラスファイルをJDなどでのぞいてみないとダメかもしれません)
　例:"mob, player, cactus, "
　       ↓
　　 "mob, player, cactus, fall"

・farDamage
　遠距離攻撃でのダメージの種類です。
　例:"arrow"
　       ↓
　   "arrow, thrown"

・itemDamage
　近接攻撃時、プレイヤーの持ってるアイテムによって出血するか否かをIDで設定します。
　数字を""の中に","で区切っていれてください
　例:"256, 300"
　       ↓
　　 "256, 300, 333, 444"


【ダメージの種類(デフォルト)】
inFire
onFire
lava
inWall
drown
starve
cactus
fall
outOfWorld
generic
explosion
magic
wither
anvil
fallingBlock
mob
player
arrow
fireball
thrown
indirectMagic


【利用規約】
・改変自由
・二次配布は改変後であれば自由
・使用・改変・二次配布は自己責任でお願いします。こちらは責任を負いかねます。
・商用利用禁止

author: ayamitsu0321（あやみつ）