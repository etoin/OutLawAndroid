package br.cea436.outlaw;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by marina on 10/03/16.
 */
public class Tiro {

    private int x;
    private int y;
    private final int altura = 20;
    private final int largura = 20;
    public final boolean jogador;
    private Drawable desenho;

    public Tiro(int x, int y, Context ctst, boolean jogador) {
        this.x = x;
        this.y = y;
        this.jogador = jogador;
        this.desenho = ctst.getResources().getDrawable(R.drawable.bola);
        this.desenho.setBounds(x, y, x+largura, y+altura);
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public Drawable getDesenho() {
        return desenho;
    }

    public void setDesenho(Drawable desenho) {
        this.desenho = desenho;
    }

    public int getRight() {
        return (x+largura);
    }

    public int getLeft() {
        return x;
    }


    public String toString()
    {return String.format("x = "+x);}
    public void AtualizaPosicao()
    {
        if(jogador)
            x += largura;
        else
            x -= largura;
        desenho.setBounds(x,y, x+largura, y+altura);
    }
}
