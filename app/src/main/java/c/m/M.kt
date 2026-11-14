package c.m
import android.graphics.*

class Col(var x: Float, var z: Float, var y: Float, var sp: Float, var ln: Int, var sg: Float, var ox: Float, var vx: Float, var rz: Float, val gl: IntArray)
class Fly(var x: Float, var y: Float, var z: Float, var vz: Float, var gl: Int, var act: Boolean)

object M {
    const val CH = "ｦｱｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝ0123456789"
    fun tc(th: Int, i: Int) = intArrayOf(
        Color.rgb(0, 255, 85), Color.rgb(0, 240, 255), Color.rgb(255, 42, 78),
        Color.rgb(255, 205, 0), Color.rgb(195, 65, 255), Color.HSVToColor(floatArrayOf((i * 23f) % 360f, 0.95f, 1f))
    )[th % 6]

    fun mkBloom(ac: Int): Bitmap {
        val rA = Color.red(ac); val gA = Color.green(ac); val bA = Color.blue(ac)
        val b = Bitmap.createBitmap(160, 160, Bitmap.Config.ARGB_8888)
        Canvas(b).drawCircle(80f, 80f, 80f, Paint(1).apply {
            shader = RadialGradient(80f, 80f, 80f, intArrayOf(Color.argb(190, 255, 255, 255), Color.argb(115, rA, gA, bA), Color.argb(35, rA, gA, bA), 0), floatArrayOf(0f, 0.30f, 0.65f, 1f), Shader.TileMode.CLAMP)
        })
        return b
    }

    fun mkSprites(th: Int, spG: ArrayList<Bitmap>, spW: ArrayList<Bitmap>) {
        spG.clear(); spW.clear()
        val tp = Paint(129).apply { typeface = Typeface.MONOSPACE; isFakeBoldText = true; textSize = 64f; textAlign = Paint.Align.CENTER }
        for (i in CH.indices) {
            val g = CH[i].toString(); val bs = tc(th, i)
            val r = Color.red(bs); val gr = Color.green(bs); val bl = Color.blue(bs)
            for (w1 in 0..1) {
                val bm = Bitmap.createBitmap(92, 92, Bitmap.Config.ARGB_8888); val cv = Canvas(bm)
                for (d in 10 downTo 1) {
                    val k = 0.14f + (1f - d / 10f) * 0.42f
                    tp.color = if (w1 == 1) Color.rgb((45 + r * k).toInt().coerceAtMost(255), (45 + gr * k).toInt().coerceAtMost(255), (45 + bl * k).toInt().coerceAtMost(255))
                    else Color.rgb((r * k * 0.6f).toInt(), (gr * k * 0.6f).toInt(), (bl * k * 0.6f).toInt())
                    cv.drawText(g, 46f + d * 1.4f, 67f + d * 1.4f, tp)
                }
                tp.color = if (w1 == 1) Color.argb(210, 255, 255, 255) else Color.argb(180, (r + 70).coerceAtMost(255), (gr + 70).coerceAtMost(255), (bl + 70).coerceAtMost(255))
                cv.drawText(g, 45.2f, 66.2f, tp)
                tp.color = if (w1 == 1) -1 else bs
                cv.drawText(g, 46f, 67f, tp)
                if (w1 == 1) spW.add(bm) else spG.add(bm)
            }
        }
    }
}
