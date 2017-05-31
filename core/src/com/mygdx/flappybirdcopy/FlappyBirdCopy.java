package com.mygdx.flappybirdcopy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;

class FlappyBirdCopy extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture background;
    private Texture gameover;
    private BitmapFont font;
    private Texture[] birds;
    private int flapState = 0;
    private float birdY = 0;
    private float velocity = 0;
    private float gravity = 2;
    private int gameState = 0;
    private Circle birdCircle;

    private int score = 0;
    private int scoringTube = 0;

    private float gap = 400;

    private  Texture topTube;
    private Texture bottomTube;

    private Random random;

    private float tubeVelocity = 4;
    private int numberOfTubes  = 4;
    private float distanceBetweenTubes;

    private float[] tubeOffset = new float[numberOfTubes];
    private float[] tubeX = new float[numberOfTubes];
    private Rectangle[] topTubeRectangles;
    private Rectangle[] bottomTubeRectangles;


    @Override
    public void create () {
        batch = new SpriteBatch();
        birdCircle = new Circle();
        background = new Texture("bg.png");
        gameover = new Texture("gameover.png");
        birds= new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        random = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        startGame();
    }

    private void startGame(){

        birdY = Gdx.graphics.getHeight()/2 - birds[flapState].getHeight()/2;

        for (int i =0; i< numberOfTubes; i++){

            tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void render () {

        batch.begin();
        batch.draw(background,0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1){

            if(tubeX[scoringTube] < Gdx.graphics.getWidth()/ 2){

                score++;
                if (scoringTube < numberOfTubes - 1){

                    scoringTube++;
                }else{

                    scoringTube = 0;
                }

            }

            if(Gdx.input.justTouched()){
                velocity = -30;
            }

            for (int i =0; i< numberOfTubes; i++){

                if (tubeX[i] < - topTube.getWidth()){

                    tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;

                }else{
                    tubeX[i] = tubeX[i] - tubeVelocity;
                }

                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap/2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i] , Gdx.graphics.getHeight() /2 - gap/2 - bottomTube.getHeight() + tubeOffset[i]);

                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap/2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() /2 - gap/2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
            }

            if(birdY > 0){

                velocity = velocity+gravity;
                birdY -= velocity ;

            }else {

                gameState = 2;

            }

        }else if(gameState == 0){
            if(Gdx.input.justTouched()){
                gameState = 1;
            }
        }else if(gameState == 2){

            batch.draw(gameover, Gdx.graphics.getWidth()/2 - gameover.getWidth()/2, Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);

            if(Gdx.input.justTouched()){
                gameState = 1;

                startGame();
                score = 0;
                scoringTube = 0;
                velocity = 0;

            }
        }

        if(flapState == 0){
            flapState = 1;
        }else{
            flapState = 0;
        }

        batch.draw(birds[flapState], Gdx.graphics.getWidth() /2 - birds[flapState].getWidth()/2, birdY);
        font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth()/7, Gdx.graphics.getHeight()/6);
        batch.end();

        birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getHeight()/2, birds[flapState].getWidth() /2);

        for (int i = 0; i < numberOfTubes; i++){
            if(Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])){
                gameState = 2;
            }
        }
    }
}
