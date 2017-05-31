package com.mygdx.flappybirdcopy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

class FlappyBirdCopy extends ApplicationAdapter {
	private SpriteBatch batch;
    private Texture background;
    private Texture[] birds;
    private int flapState = 0;
    private float birdY = 0;
    private float velocity = 0;
    private float gravity = 2;
    private int gameState = 0;
    private Texture topTube;
    private Texture bottomTube;
    private float gap = 400;
    private float maxTubeOffset;
    private Random random;
    private  float tubeVelocity = 4;
    private float distanceBetweenTubes;
    private int numberOfTubes = 4;
    private float tubeOffset[] = new float[numberOfTubes];
    private float tubeX[] = new float[numberOfTubes];


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
        birdY = Gdx.graphics.getHeight() /2 - birds[flapState].getHeight() / 2;
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        random = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;

        for(int i = 0; i < numberOfTubes; i++){
            tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + i * distanceBetweenTubes;
        }
	}

	@Override
	public void render () {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if(gameState != 0){

            if(Gdx.input.justTouched()){
                velocity = -30;
            }

            for(int i = 0; i < numberOfTubes; i++){
                if(tubeX[i] < - topTube.getWidth()){
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                }

                tubeX[i] = tubeX[i] - tubeVelocity;
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight()+ tubeOffset[i]);
            }



            if(birdY > 0 || velocity < 0){
                velocity += gravity;
                birdY -= velocity;
            }

        }else{
            if(Gdx.input.justTouched()){
                gameState = 1;
            }
        }

        if(flapState == 0){
            flapState = 1;
        }
        else{
            flapState = 0;
        }



        batch.draw(birds[flapState], Gdx.graphics.getWidth() /2 - birds[flapState].getWidth() / 2, birdY);
        batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
