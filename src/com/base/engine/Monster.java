package com.base.engine;

import java.util.ArrayList;
import java.util.Random;

public class Monster {
    public static final float SCALE = 0.7f;

    public static final float SIZEY = SCALE;
    public static final float SIZEX = (float)((double)SIZEY / (1.9310344837686206896551724137931 * 2.0));
    public static final float START = 0;

    public static final float TEX_MAX_X = 1;
    public static final float TEX_MAX_Y = 1;
    public static final float TEX_MIN_X = 0;
    public static final float TEX_MIN_Y = 0;

    public static final int STATE_IDLE = 0;
    public static final int STATE_CHASE = 1;
    public static final int STATE_ATTACK = 2;
    public static final int STATE_DYING = 3;
    public static final int STATE_DEAD = 4;

    public static final float MOVE_SPEED = 1;
    public static final float MOVEMENT_STOP_DISTANCE = 1.5f;
    public static final float MONSTER_WIDTH = 0.2f;
    public static final float MONSTER_LENGTH = 0.2f;
    public static final float SHOOT_DISTANCE = 1000.0f;
    public static final float SHOT_ANGLE = 10.0f;
    public static final float ATTACK_CHANCE = 0.05f;
    public static final int MAX_HEALTH = 100;
    public static final int DAMAGE_MIN = 5;
    public static final int DAMAGE_MAX = 25;
    public static final double HIT_STUN_DURATION = 0.23;

    private double deathTime;

    private static ArrayList<Texture> animations;
    private static Mesh mesh;

    private Material material;
    private Transform transform;
    private Random rand;
    private int state;
    private boolean canLook;
    private boolean canAttack;
    private int health;
    private boolean hitStun;
    private double hitStunStart;

    public Monster(Transform transform){
        if(animations == null){
            animations = new ArrayList<Texture>();

            animations.add(new Texture("SSWVA1.png"));
            animations.add(new Texture("SSWVB1.png"));
            animations.add(new Texture("SSWVC1.png"));
            animations.add(new Texture("SSWVD1.png"));

            animations.add(new Texture("SSWVE0.png"));
            animations.add(new Texture("SSWVF0.png"));
            animations.add(new Texture("SSWVG0.png"));

            animations.add(new Texture("SSWVH0.png"));

            animations.add(new Texture("SSWVI0.png"));
            animations.add(new Texture("SSWVJ0.png"));
            animations.add(new Texture("SSWVK0.png"));
            animations.add(new Texture("SSWVL0.png"));

            animations.add(new Texture("SSWVM0.png"));
        }

        if(mesh == null){
            Vertex[] vertices = new Vertex[]{
                    new Vertex(new Vector3f(-SIZEX,START,START), new Vector2f(TEX_MAX_X,TEX_MAX_Y)),
                    new Vertex(new Vector3f(-SIZEX,SIZEY,START), new Vector2f(TEX_MAX_X,TEX_MIN_Y)),
                    new Vertex(new Vector3f(SIZEX,SIZEY,START), new Vector2f(TEX_MIN_X,TEX_MIN_Y)),
                    new Vertex(new Vector3f(SIZEX,START,START), new Vector2f(TEX_MIN_X,TEX_MAX_Y))
            };

            int[] indices = new int[]{0,1,2,
                                        0,2,3};

            mesh = new Mesh(vertices, indices);
        }

        this.transform = transform;
        this.state = STATE_IDLE;
        this.canLook = false;
        this.canAttack = false;
        this.health = MAX_HEALTH;
        this.rand = new Random();
        this.material = new Material(animations.get(0));
        this.deathTime = 0;
        this.hitStun = false;
        this.hitStunStart = 0;
    }

    public void damage(int amount){
        hitStunStart = 0;
        health -= amount;

        if(health <= 0){
            state = STATE_DYING;
        }
        else {
            this.hitStun = true;
            material.setTexture(animations.get(7));
        }
    }

    private void idleUpdate(Vector3f orientation, float distance){
        double time = Time.getTime()/(double)Time.SECOND;
        double timeDecimals = time - (double)((int)time);

        if(timeDecimals < 0.5){
            canLook = true;
            material.setTexture(animations.get(0));
        }
        else{
            material.setTexture(animations.get(1));
            if(canLook){

                Vector2f lineStart = transform.getTranslation().getXZ();
                Vector2f castDirection = orientation.getXZ();
                Vector2f lineEnd = lineStart.add(castDirection.mul(SHOOT_DISTANCE));

                Vector2f collisionVector = Game.getLevel().checkIntersections(lineStart, lineEnd, false);

                Vector2f playerIntersectVector = Game.getLevel().lineIntersectRect(lineStart, lineEnd, Transform.getCamera().getPos().getXZ(), new Vector2f(Player.PLAYER_SIZE, Player.PLAYER_SIZE));

                if(playerIntersectVector != null &&
                        (collisionVector == null ||
                                playerIntersectVector.sub(lineStart).length() < collisionVector.sub(lineStart).length())){
                    System.out.println("Seen player");
                    state = STATE_CHASE;
                }

                canLook = false;
            }
        }

    }

    private void chaseUpdate(Vector3f orientation, float distance){
        double time = Time.getTime()/(double)Time.SECOND;
        double timeDecimals = time - (double)((int)time);

        if(timeDecimals < 0.25){
            material.setTexture(animations.get(0));
        }
        else if(timeDecimals < 0.5){
            material.setTexture(animations.get(1));
        }
        else if(timeDecimals < 0.75){
            material.setTexture(animations.get(2));
        }
        else {
            material.setTexture(animations.get(3));
        }

        if(rand.nextDouble() < ATTACK_CHANCE * Time.getDelta()){
            state = STATE_ATTACK;
        }

        if(distance > MOVEMENT_STOP_DISTANCE){
            float moveAmount = MOVE_SPEED * (float)Time.getDelta();
            Vector3f oldPos = transform.getTranslation();
            Vector3f newPos = transform.getTranslation().add(orientation.mul(moveAmount));

            Vector3f collisionVector = Game.getLevel().checkCollision(oldPos, newPos, MONSTER_WIDTH, MONSTER_LENGTH);

            Vector3f movementVector = collisionVector.mul(orientation);

            if(movementVector.sub(orientation).length() != 0)
                Game.getLevel().openDoors(transform.getTranslation());
            if(movementVector.length() > 0)
                transform.setTranslation(transform.getTranslation().add(movementVector.mul(moveAmount)));
        } else {
            state = STATE_ATTACK;
        }
    }

    private void attackUpdate(Vector3f orientation, float distance){
        double time = Time.getTime()/(double)Time.SECOND;
        double timeDecimals = time - (double)((int)time);

        if(timeDecimals < 0.25){
            material.setTexture(animations.get(4));
        }
        else if(timeDecimals < 0.5){
            material.setTexture(animations.get(5));
        }
        else if(timeDecimals < 0.75){
            material.setTexture(animations.get(6));
            if (canAttack) {
                Vector2f lineStart = transform.getTranslation().getXZ();
                Vector2f castDirection = orientation.getXZ().rotate((rand.nextFloat() - 0.5f) * SHOT_ANGLE);
                Vector2f lineEnd = lineStart.add(castDirection.mul(SHOOT_DISTANCE));

                Vector2f collisionVector = Game.getLevel().checkIntersections(lineStart, lineEnd, false);

                Vector2f playerIntersectVector = Game.getLevel().lineIntersectRect(lineStart, lineEnd, Transform.getCamera().getPos().getXZ(), new Vector2f(Player.PLAYER_SIZE, Player.PLAYER_SIZE));

                if (playerIntersectVector != null &&
                        (collisionVector == null ||
                                playerIntersectVector.sub(lineStart).length() < collisionVector.sub(lineStart).length())) {
                    System.out.println("Hit player");
                    Game.getLevel().damagePlayer(rand.nextInt(DAMAGE_MAX - DAMAGE_MIN) + DAMAGE_MIN);
                }

                state = STATE_CHASE;
                canAttack = false;
            }
        } else {
            canAttack = true;
            material.setTexture(animations.get(5));
        }
    }

    private void dyingUpdate(Vector3f orientation, float distance){
        deathTime += Time.getDelta();

        if(deathTime < 0.1f){
            material.setTexture(animations.get(8));
            transform.setScale(1,0.96428571428571428571428571428571f,1);
        }
        else if(deathTime < 0.3f){
            material.setTexture(animations.get(9));
            transform.setScale(1.7f,0.9f,1);
        }
        else if(deathTime < 0.45f){
            material.setTexture(animations.get(10));
            transform.setScale(1.7f,0.9f,1);
        }
        else if(deathTime < 0.6f){
            material.setTexture(animations.get(11));
            transform.setScale(1.7f,0.5f,1);
        } else {
            state = STATE_DEAD;
        }
    }

    private void deadUpdate(Vector3f orientation, float distance){
        material.setTexture(animations.get(12));
        transform.setScale(1.75862068965517241379310f, 0.285714285714285714f, 1f);
    }

    private void alignWithGround(){
        transform.setTranslation(transform.getTranslation().getX(), 0.0f, transform.getTranslation().getZ());
    }

    private void billboard(Vector3f directionToCam){

        float angleToFaceCamera = (float)Math.toDegrees(Math.atan(directionToCam.getZ()/directionToCam.getX()));

        if(directionToCam.getX() < 0)
            angleToFaceCamera += 180.0f;

        transform.getRotation().setY(angleToFaceCamera + 90.0f);
    }

    public void update(){
        Vector3f directionToCam = Transform.getCamera().getPos().sub(transform.getTranslation());
        float distance = directionToCam.length();
        Vector3f orientation = directionToCam.div(distance);

        billboard(orientation);
        if(!hitStun){
            switch (state){
                case STATE_IDLE:
                    idleUpdate(orientation, distance);
                    break;
                case STATE_CHASE:
                    chaseUpdate(orientation, distance);
                    break;
                case STATE_ATTACK:
                    attackUpdate(orientation, distance);
                    break;
                case STATE_DYING:
                    dyingUpdate(orientation, distance);
                    break;
                case STATE_DEAD:
                    deadUpdate(orientation, distance);
                    break;
            }
        } else {
            hitStunStart += Time.getDelta();
            if(hitStunStart > HIT_STUN_DURATION){
                if(state == STATE_IDLE){
                    state = STATE_CHASE;
                }
                hitStun = false;
            }
        }


        alignWithGround();
    }

    public void render(){
        Shader shader = Game.getLevel().getShader();
        shader.bind();

        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
        shader.unbind();
    }

    public Vector2f getSize(){
        return new Vector2f(MONSTER_WIDTH, MONSTER_LENGTH);
    }

    public Transform getTransform(){
        return transform;
    }
}
