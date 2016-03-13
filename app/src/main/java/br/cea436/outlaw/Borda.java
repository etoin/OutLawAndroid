package br.cea436.outlaw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * Created by marina on 12/03/16.
 */
public class Borda extends View {

    private Paint cor;
    private int altraTela, larguraTela;
    public final int distanciaTela = 10;


    public Borda(Context context) {
        super(context, null);
        cor = new Paint();
        cor.setARGB(255,135,206,235);
    }

    @Override
    //Callback para quando a tela é iniciada ou redimensionada
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        altraTela = height;
        larguraTela = width;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Log.i("out", "Altura  " + altraTela);
        canvas.drawRect(distanciaTela, distanciaTela, 20, altraTela - distanciaTela, cor); //esquerda
        canvas.drawRect(larguraTela-20, distanciaTela, larguraTela-distanciaTela, altraTela-distanciaTela, cor); //direita
        canvas.drawRect(distanciaTela, distanciaTela, larguraTela-distanciaTela, 20, cor); //teto
        canvas.drawRect(distanciaTela, altraTela-20, larguraTela-distanciaTela, altraTela-distanciaTela, cor); //chao
    }
}
