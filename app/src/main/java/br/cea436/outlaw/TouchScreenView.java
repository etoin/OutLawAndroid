package br.cea436.outlaw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class TouchScreenView extends View {

    private static final String CATEGORIA = "OutLaw";
    private Drawable img;
    private Drawable img2;
    private Drawable cactus;
    private Drawable tiro, adversario;
    private ArrayList<Tiro> arrayTiro = new ArrayList<Tiro>();
    private Context context;
    private Handler rHandler, aHandler;
    private Borda borda;
    private boolean alguemMorto = false;

    int x,y, xc, yc, xt, yt, xa, ya;
    int imagem;
    private boolean selecionou;
    private int larguraTela, alturaTela, larguraImg, alturaImg, larguraImgCactus, alturaImgCactus;

    public TouchScreenView(Context context) {
        super(context,null);

        borda = new Borda(context);
        imagem = 1;
        this.context = context;
        img = context.getResources().getDrawable(R.drawable.aberto);
        img2 = context.getResources().getDrawable(R.drawable.fechado);
        adversario = context.getDrawable(R.drawable.abertoesq);

        cactus = context.getResources().getDrawable(R.drawable.cactus);
        tiro = context.getResources().getDrawable(R.drawable.bola);


        //Recupera dimensoes da imagem
        larguraImg = img.getIntrinsicWidth();
        alturaImg = img.getIntrinsicHeight();

        larguraImgCactus = cactus.getIntrinsicWidth();
        alturaImgCactus = cactus.getIntrinsicHeight();

        rHandler =  new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //chamo um método para melhor organização.
                updateUI(msg);
            }
        };
        aHandler=  new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //chamo um método para melhor organização.
                updatePos(msg);
            }
        };
        Worker w = new Worker(rHandler);
        w.start();



        setFocusable(true);
    }

    @Override
    //Callback para quando a tela é iniciada ou redimensionada
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        borda.onSizeChanged(width, height, oldw, oldh);
        this.alturaTela = height;
        this.larguraTela = width/2;
        x = width/4 - (larguraImg/2);
        y = height/4 - (alturaImg/2);

        xc = width/2 - (larguraImgCactus/2);
        yc = height/2 - (alturaImgCactus/2);

        xt = width/3 - 10;
        yt = height/3 - 10;

        xa = 2*width/3 - (larguraImg/2);
        ya = 2*height/4 - (alturaImg/2);
        Adversario Adv = new Adversario(aHandler);
        Adv.start();
        Log.i(CATEGORIA, "onSizeChanged x:y = " + x + ":" + y);
    }

    private void updateUI(Message m)
    {
        if(m.arg1 == 1) {
            ArrayList<Tiro> remover = new ArrayList<>();
            for (Tiro tiro : arrayTiro) {
                tiro.AtualizaPosicao();
                int res = removeTiro(tiro);
                Log.i(CATEGORIA, "Resposta = " + res);
                if (res == 1)
                    remover.add(tiro);
                else if (res > 0) {
                    alguemMorto = true;
                    if (res == 2)
                        img = context.getDrawable(R.drawable.sentado);
                    else
                        adversario = context.getDrawable(R.drawable.sentadoesq);
                    remover.clear();
                    arrayTiro.clear();
                    break;
                }
            }
            for (int j = remover.size() - 1; j >= 0; j--)
                if (arrayTiro.remove(remover.get(j)))
                    Log.i(CATEGORIA, "");
            //Log.i(CATEGORIA, arrayTiro.toString());
            invalidate();
        }
        else
        {
            x = (larguraTela*2)/4 - (larguraImg/2);
            y = alturaTela/4 - (alturaImg/2);

            xc = (larguraTela*2)/2 - (larguraImgCactus/2);
            yc = alturaTela/2 - (alturaImgCactus/2);

            xt = (larguraTela*2)/3 - 10;
            yt = alturaTela/3 - 10;

            xa = 2*(larguraTela*2)/3 - (larguraImg/2);
            ya = 2*alturaTela/4 - (alturaImg/2);

            img = context.getDrawable(R.drawable.aberto);
            adversario = context.getDrawable(R.drawable.abertoesq);
            alguemMorto = false;
            arrayTiro.clear();
            invalidate();
        }
    }

    private int removeTiro(Tiro tiro)
    {
        /*
        * 1 - Remove
        * 2 - Acertou jogador
        * 3 - Acertou CPU
        * 0 - Nao remove
        * */
        int R = tiro.getRight();
        int Y = tiro.getY();
        int L = tiro.getLeft();
        if(tiro.jogador) {
            if(R >= adversario.copyBounds().left && R <= adversario.copyBounds().right && Y >= adversario.copyBounds().top && Y <= adversario.copyBounds().bottom)
                return 3;
            else if (R >= larguraTela * 2 - borda.distanciaTela * 2) {
                // Log.i(CATEGORIA, "removeu");
                return 1;
            } else if (Y > (cactus.copyBounds().top + 66) && Y < (cactus.copyBounds().bottom - 100)) {//braço do cacto
                if (R >= cactus.copyBounds().left)
                    return 1;

            }
            else if(Y >= cactus.copyBounds().top && Y <= cactus.copyBounds().bottom )
                if(R >= 625)
                    return 1;
        }
        else
        {
            if(L <= img.copyBounds().right&& L >= img.copyBounds().left && Y >= img.copyBounds().top && Y <= img.copyBounds().bottom)
                return 2;
            else if (L <= borda.distanciaTela * 2) {
                // Log.i(CATEGORIA, "removeu");
                return 1;
            } else if (Y < (cactus.copyBounds().top+100) && Y > (cactus.copyBounds().top)) {//braço do cacto
                if (L <= cactus.copyBounds().right)
                    return 1;

            }
            else if(Y >= cactus.copyBounds().top+100 && Y <= cactus.copyBounds().bottom )
                if(L <= 655)
                    return 1;
        }
        return 0;
    }

    private void updatePos(Message msg)
    {
        if(!alguemMorto)
        {
            xa = msg.arg1;
            ya = msg.arg2;
            int img = (int) msg.obj;
            if (img == 1)
                adversario = context.getDrawable(R.drawable.abertoesq);
            else if (img == 2)
                adversario = context.getDrawable(R.drawable.fecchadoessq);
            else if (img == 3) {
                adversario = context.getDrawable(R.drawable.agachadoesq);
                int xtt = xa;
                int ytt = ya + alturaImg / 3;
                arrayTiro.add(new Tiro(xtt, ytt, context, false));
            }

            invalidate();
        }
    }
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint pincel = new Paint();
        pincel.setColor(Color.TRANSPARENT);
        canvas.drawRect(0, 0, larguraTela, alturaTela, pincel);
        borda.draw(canvas);

        //Definição da área passível de desenho
        img.setBounds(x, y, x + larguraImg, y + alturaImg);
        adversario.setBounds(xa, ya, xa + larguraImg, ya + alturaImg);
        cactus.setBounds(xc, yc, xc + larguraImgCactus, yc + alturaImgCactus);


        cactus.draw(canvas);
        img.draw(canvas);
        adversario.draw(canvas);

        for(Tiro t :arrayTiro){
            t.getDesenho().draw(canvas);
        }

    }

    @Override
    //Move a imagem
    public boolean onTouchEvent(MotionEvent event) {
        if(!alguemMorto) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //Inicia movimento se pressionou a imagem

                    selecionou = img.copyBounds().contains((int) x, (int) y);
                    if (!selecionou) {
                        img = context.getResources().getDrawable(R.drawable.agachado);
                        int xtt = this.x + larguraImg;
                        int ytt = this.y + alturaImg / 3;
                        arrayTiro.add(new Tiro(xtt, ytt, context, true));

                    }

                    Log.i(CATEGORIA, "ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    //Arrasta o boneco
                    if (selecionou) {
                        int xaux = (int) x - (larguraImg / 2);
                        int yaux = (int) y - (alturaImg / 2);
                        int distancia = borda.distanciaTela;
                        if ((xaux < larguraTela - larguraImg - larguraImgCactus / 2) && (xaux > 2 * distancia && yaux > 2 * distancia && (yaux + alturaImg) < alturaTela - 2 * distancia)) {
                            this.x = xaux;
                            this.y = yaux;

                            if (imagem == 1) {
                                img = context.getResources().getDrawable(R.drawable.aberto);
                                imagem++;
                            } else if (imagem == 20) {
                                img = context.getResources().getDrawable(R.drawable.fechado);
                                imagem++;
                            } else if (imagem == 40)
                                imagem = 1;
                            else
                                imagem++;

                            Log.i(CATEGORIA, "ACTION_MOVE");
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //Finaliza movimento
                    img = context.getResources().getDrawable(R.drawable.aberto);
                    selecionou = false;
                    Log.i(CATEGORIA, "ACTION_UP");
                    break;
            }
            invalidate();
        }
        return true;
    }

    class Worker extends Thread{

        private Handler sHandler;
        public Worker(Handler han)
        {
            sHandler = han;
        }

        public void run(){
            while (true) {
                try
                {
                    if(alguemMorto)
                    {
                        Thread.sleep(500);
                        Message msg = new Message();
                        msg.arg1 = 2;
                        sHandler.sendMessage(msg);
                    }
                    else
                    {
                        Thread.sleep(100);
                        Message msg = new Message();
                        msg.arg1 = 1;
                        sHandler.sendMessage(msg);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Adversario extends Thread
    {
        private Handler moveHandler;
        public Adversario(Handler handler)
        {
            moveHandler = handler;
        }

        public void run()
        {

            try

            {

                int imgAdv = 0;
                while (true)
                {
                    Random random  = new Random();
                    int newX;
                    int newY;
                    int distancia = borda.distanciaTela*2;
                    boolean cacto;
                    boolean teto;
                    boolean chao;
                    boolean parede;
                    do {
                        newX = xa + random.nextInt(201) - 100;
                        newY = ya + random.nextInt(201) - 100;

                        cacto = newX > larguraTela+larguraImgCactus/2;
                        teto = newY > distancia;
                        chao = newY+alturaImg < alturaTela-distancia;
                        parede = (newX+larguraImg) < (larguraTela*2)-distancia;

                    }while (!(cacto && parede && chao && teto));
                    int sorce = 0;

                    if (imgAdv == 1)
                    {
                        sorce = 1;
                        imgAdv++;
                    } else if (imgAdv == 20)
                    {
                        sorce = 2;
                        imgAdv++;
                    } else if (imgAdv == 40) {
                        imgAdv = 1;
                        sorce = 1;
                    }
                    else
                        imgAdv++;
                    Log.i(CATEGORIA, "image = "+imgAdv);
                    if((ya + alturaImg / 3) >= y && (ya + alturaImg / 3) < (y+alturaImg))
                    {
                        sorce = 3;
                        imgAdv = 1;
                    }
                    Message msg = new Message();
                    msg.obj = sorce;
                    msg.arg1 = newX;
                    msg.arg2 = newY;
                    moveHandler.sendMessage(msg);
                    Thread.sleep(200);
                    if(sorce == 3)
                        Thread.sleep(200);
                }

            }
            catch (InterruptedException e)
            {
            }
        }

    }
}
