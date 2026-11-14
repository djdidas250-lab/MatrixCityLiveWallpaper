package c.m
import android.graphics.*

class Col(var x: Float, var z: Float, var y: Float, var sp: Float, var ln: Int, var sg: Float, var ox: Float, var vx: Float, var rz: Float, val gl: IntArray)
class Fly(var x: Float, var y: Float, var z: Float, var vz: Float, var gl: Int, var act: Boolean)

object M {
    const val CH = "ｦｱｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝ0123456789"
    fun tc(th: Int, i: Int) = when (th % 6) {
        0 -> Color.rgb(0, 255, 85)
        1 -> Color.rgb(0, 240, 255)
        2 -> Color.rgb(255, 42, 78)
        3 -> Color.rgb(255, 205, 0)
        4 -> Color.rgb(195, 65, 255)
        else -> Color.HSVToColor(floatArrayOf((i * 23f) % 360f, 0.95f, 1f))
    }

    fun mkBloom(ac: Int): Bitmap {
        val rA = Color.red(ac); val gA = Color.green(ac); val bA = Color.blue(ac)
        val b = Bitmap.createBitmap(160, 160, Bitmap.Config.ARGB_8888)
        Canvas(b).drawCircle(80f, 80f, 80f, Paint(1).apply {
            shader = RadialGradient(80f, 80f, 80f, intArrayOf(Color.argb(225, 255, 255, 255), Color.argb(145, rA, gA, bA), Color.argb(40, rA, gA, bA), 0), floatArrayOf(0f, 0.28f, 0.62f, 1f), Shader.TileMode.CLAMP)
        })
        return b
    }

    fun mkSprites(th: Int, spG: ArrayList<Bitmap>, spW: ArrayList<Bitmap>) {
        spG.clear(); spW.clear()
        val tp = Paint(129).apply { typeface = Typeface.MONOSPACE; isFakeBoldText = true; textSize = 64f; textAlign = Paint.Align.CENTER }
        for (i in CH.indices) {
            val g = CH.elementAt(i).toString(); val bs = tc(th, i)
            val r = Color.red(bs); val gr = Color.green(bs); val bl = Color.blue(bs)
            for (w1 in 0..1) {
                val bm = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888); val cv = Canvas(bm)
                for (d in 11 downTo 1) {
                    val k = 0.12f + (1f - d / 11f) * 0.44f
                    tp.color = if (w1 == 1) Color.rgb((45 + r * k).toInt().coerceAtMost(255), (45 + gr * k).toInt().coerceAtMost(255), (45 + bl * k).toInt().coerceAtMost(255))
                    else Color.rgb((r * k * 0.58f).toInt(), (gr * k * 0.58f).toInt(), (bl * k * 0.58f).toInt())
                    cv.drawText(g, 48f + d * 1.45f, 70f + d * 1.45f, tp)
                }
                tp.color = if (w1 == 1) Color.argb(220, 255, 255, 255) else Color.argb(190, (r + 75).coerceAtMost(255), (gr + 75).coerceAtMost(255), (bl + 75).coerceAtMost(255))
                cv.drawText(g, 47.2f, 69.2f, tp)
                tp.color = if (w1 == 1) -1 else bs
                cv.drawText(g, 48f, 70f, tp)
                if (w1 == 1) spW.add(bm) else spG.add(bm)
            }
        }
    }
}
