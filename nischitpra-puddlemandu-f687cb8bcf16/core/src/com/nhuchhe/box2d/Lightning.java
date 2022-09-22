package com.nhuchhe.box2d;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by nischit on 31/7/17.
 */

public class Lightning implements Pool.Poolable{
    private MyGame game;
    private Node head;
    private Body body;
    public float displacement;
    private final int MAX_DEPTH=4;
    private long lastGeneration;
    private long lastLightning;


    public Lightning(MyGame game){
        this.game=game;
    }

    public void generateTree(Vector2 position,float vel){
        displacement=vel;
        if(TimeUtils.nanoTime()-lastGeneration>Constants.TAP) {
            // first remove the bodies then only create new ones
            destroy(head);

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            body = game.world.createBody(bodyDef);
            body.setGravityScale(0);
            body.setUserData(Constants.BOLT);

            head=new Node();
            head.init(game, body);
            head.createPosition(position);
            generateChildNode(head, 0);
            treeBuilder(head.getLeft(), 0);
            treeBuilder(head.getRight(), 0);

            lastGeneration = game.utils.randomTime(0.03f,0.3f);
            if(lastGeneration>TimeUtils.nanoTime()+0.15f*Constants.NANOSECOND){
                if(TimeUtils.nanoTime()>lastLightning){
                    game.audioManager.play("lightning_"+MathUtils.random(0,4),false);
                    lastLightning=game.utils.randomTime(0.85f,0.85f);
                }
            }
        }else{
            if(body!=null){
                body.setTransform(body.getPosition().x+vel,body.getPosition().y,0);
            }
        }
    }


    public Node getHead(){return head;}
    private void treeBuilder(Node parent,int currentDepth){
        if(currentDepth==MAX_DEPTH) return;
        if(parent==null) return;

        generateChildNode(parent,currentDepth);
        treeBuilder(parent.getLeft(),currentDepth+1);
        treeBuilder(parent.getRight(),currentDepth+1);
    }
    private void generateChildNode(Node parent,int depth){
        // at greater random chance create a node
        if(MathUtils.random(0,1)==0) {
            Node n=new Node();
            n.init(game,body);
            n.createPosition(parent.getChildPosition());
            parent.setLeft(n);
        }
        // create another branch at random chance
        if(MathUtils.random(0,depth*2)==0){
            Node n=new Node();
            n.init(game,body);
            n.createPosition(parent.getChildPosition());
            parent.setRight(n);
        }
    }
    public void render(){
        renderGlow(head);
        renderNode(head);
    }

    public void renderNode(Node parent){
        if(parent== null) return;
        parent.render(displacement);

        renderNode(parent.getLeft());
        renderNode(parent.getRight());
    }
    public void renderGlow(Node parent){
        if(parent== null) return;
        parent.renderGlow();

        renderGlow(parent.getLeft());
        renderGlow(parent.getRight());
    }
    //  used to clean up bodies when rendering new nodes
    public void destroy(Node parent){
        if(parent==null) return;
        if(body==null) return;
        head=null;
        Constants.deadBodies.add(body);
        body=null;
    }
    public void dispose(Node parent){
        if(parent==null) return;
        if(body==null) return;
        head=null;
        game.world.destroyBody(body);
        body=null;
    }

    @Override
    public void reset() {
        head=null;
        body=null;
        displacement=0;
        lastGeneration=0;
    }
}
