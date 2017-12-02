import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class Bomberman here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bomberman extends InteractableActor
{
    private PlayerColor playerColor = PlayerColor.White;
    
    private int maxNumOfBombs = 3;
    
    private int movementSpeed = 2;
    
    private int lifes = 3;
    
    private Bomb templateBomb = new Bomb();
    
    private boolean isDead = false;

    private List<Bomb> thrownBombsList = new ArrayList<Bomb>();
    //for statistics
    private int numBombThrown = 0;
    
    private int numKills = 0;

    private MovementDirection currentDirection = MovementDirection.Down;
    
    private int invulnerabilityTime = 150;
    
    private boolean isInvulnerable = false;
 
    private int invulnerabilityTimeCounter = 0;
    
    Bomberman()
    {
        forceGridLocation = false;
        templateBomb.setBesitzer(this);
    }
    
    /**
     * Bomberman Constructor
     *
     * @param newPlayerColor A parameter
     * @param newMaxNumOfBombs A parameter
     * @param newMovementSpeed A parameter
     * @param newLifes A parameter
     */
    Bomberman(PlayerColor newPlayerColor,int newMaxNumOfBombs, int newMovementSpeed,int newLifes)
    {
        forceGridLocation = false;
        playerColor = newPlayerColor;
        newMaxNumOfBombs = newMaxNumOfBombs;
        templateBomb.setBesitzer(this);
        movementSpeed = newMovementSpeed;
        lifes = newLifes;
    }
    
    public void addThrownBomb(Bomb newBomb)
    {
        thrownBombsList.add(newBomb);
    }
    
    public void removeThrownBomb(Bomb removeBomb)
    {
        thrownBombsList.remove(removeBomb);
    }
    /**
     * @return the maxNumOfBombs
     */
    public PlayerColor getPlayerColor() 
    {
        return playerColor;

    }

    /**
     * @param maxNumOfBombs the maxNumOfBombs to set
     */
    public void setPlayerColor(PlayerColor color) 
    {
        playerColor = color;
        setDirectionImage(currentDirection);
    }

    /**
     * @return the maxNumOfBombs
     */
    public int getMaxNumOfBombs() 
    {
      
        return maxNumOfBombs;
    }

    /**
     * @param maxNumOfBombs the maxNumOfBombs to set
     */
    public void setMaxNumOfBombs(int maxNumOfBombs) 
    {
          if(maxNumOfBombs <= 1) maxNumOfBombs = 1; 
        this.maxNumOfBombs = maxNumOfBombs;
    }

    /**
     * @return the movementSpeed
     */
    public int getMovementSpeed() 
    {
        return movementSpeed;
    }

    /**
     * @param movementSpeed the movementSpeed to set
     */
    public void setMovementSpeed(int movementSpeed) 
    {
        if(movementSpeed <= 2) movementSpeed = 2;
        if(movementSpeed >= 5) movementSpeed = 5;
        this.movementSpeed = movementSpeed;
    }

    /**
     * @return the lifes
     */
    public int getLifes() 
    {
        return lifes;
    }

    /**
     * @param lifes the lifes to set
     */
    public void setLifes(int lifes) 
    {
        this.lifes = lifes;
        if(lifes <= 0)
        {
            OnDeath();
        }
    }

    /**
     * @return the templateBomb
     */
    public Bomb getTemplateBomb()
    {
        return templateBomb;
    }

    /**
     * @param templateBomb the templateBomb to set
     */
    public void setTemplateBomb(Bomb templateBomb) 
    {
        this.templateBomb = templateBomb;
    }

    /**
     * @return the isDead
     */
    public boolean isDead() 
    {
        return isDead;
    }

    /**
     * @param isDead the isDead to set
     */
    public void setDead(boolean isDead)
    {
        this.isDead = isDead;
    }

    /**
     * @return the numBombThrown
     */
    public int getNumBombThrown() 
    {
        return numBombThrown;
    }

    /**
     * @param numBombThrown the numBombThrown to set
     */
    public void setNumBombThrown(int numBombThrown)
    {
        this.numBombThrown = numBombThrown;
    }

    /**
     * @return the numKills
     */
    public int getNumKills() 
    {
        return numKills;
    }

    /**
     * @param numKills the numKills to set
     */
    public void setNumKills(int numKills)
    {
        this.numKills = numKills;
    }

    /**
     * @return the currentDirection
     */
    public MovementDirection getCurrentDirection() 
    {
        return currentDirection;
    }

    /**
     * @param currentDirection the currentDirection to set
     */
    public void setCurrentDirection(MovementDirection currentDirection)
    {
        this.currentDirection = currentDirection;
        setDirectionImage(currentDirection);
    }

    /**
     * Act - do whatever the Bomberman wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        findPowerUp();
        if(isInvulnerable)
        {
            invulnerabilityTimeCounter--;
            if(invulnerabilityTimeCounter <= 0)
            {
                OnEndInvulnerability();
            }
        }
    }  
    protected void OnStartInvulnerability()
    {
        invulnerabilityTimeCounter = invulnerabilityTime;
        isInvulnerable = true;
        setDirectionImage(currentDirection);
    }
    
    protected void OnEndInvulnerability()
    {
        isInvulnerable = false;
        setDirectionImage(currentDirection);
    }
    
    /**
     * Method onDeath
     *
     */
    protected void onDeath()
    {
        bomberWorld.removeObject(this);
    }

    protected void setDirectionImage(MovementDirection dir)
    {

        BomberWorld w = (BomberWorld)getWorld();
        if(isInvulnerable == true)
        {
            setImage(w.getStyleSheet().getBombermanInvulnerableImage(playerColor,dir));
        }
        else
        {
            setImage(w.getStyleSheet().getBombermanImage(playerColor,dir));
        }
        
    }

    protected boolean canMoveAtPos(MovementDirection direction,int x,int y)
    {
        int currentX = getX();
        int currentY = getY();

        setLocation(x,y);
        if(canMove(direction))
        {
            setLocation(currentX,currentY);
            return true;
        }
        else
        {
            setLocation(currentX,currentY);
            return false;
        }
    }
    
    protected int maxMoveDistance(MovementDirection newDirection)
    {
        for(int i = 1; i <= movementSpeed;i++)
        {

            List<Obstacle> blocking = new ArrayList<Obstacle>();
            switch(newDirection)
            {
                case Up:
                {
                    setLocation(getX(),getY() - i);
                    blocking.addAll(getIntersectingObjects(Obstacle.class));
                    setLocation(getX(),getY() + i );

                }break;
                case Down:
                {
                    setLocation(getX(),getY() + i);
                    blocking.addAll(getIntersectingObjects(Obstacle.class));
                    setLocation(getX(),getY() - i);
                }break;
                case Left:
                {
                    setLocation(getX() - i,getY() );
                    blocking.addAll(getIntersectingObjects(Obstacle.class));
                    setLocation(getX() + i,getY() );
                }break;
                case Right:
                {
                    setLocation(getX() + i,getY() );
                    blocking.addAll(getIntersectingObjects(Obstacle.class));
                    setLocation(getX() - i,getY() );
                }break;

            }
            if(blocking.size() != 0) 
            {               
                return i -1;
            }
        }
        return movementSpeed;
    }

    /**
     * Method canMove
     *
     * @param newDirection A parameter
     * @return The return value
     */
    protected boolean canMove(MovementDirection newDirection)
    {
        return maxMoveDistance(newDirection) > 0;
    }

    

    protected void decideMoveY(MovementDirection dir)
    {
        //Überprüfe ob wir hoch können
        if(canMove(dir) == true)
        {
            
            move(dir);
            return;
        }
        //überprüfe ob wir perfekt in unseren grid hoch können

        if(canMoveAtPos(dir,getGridXPosAsPixel(),getGridYPosAsPixel()))
        {
            int deltaX = getGridXPosAsPixel() - getX();
            int remainingMovement =  movementSpeed - Math.abs(deltaX);
            if(remainingMovement <= 0)
            {
               
                if(deltaX > 0)
                {
                    move(MovementDirection.Right,movementSpeed);
                }
                else
                {
                    move(MovementDirection.Left,movementSpeed);
                }

                return;
            }
            else
            {
               
                move(MovementDirection.Right,deltaX);
                move(dir,remainingMovement);
                return;
            }
        }
        //Überprüfe ob wir links oder rechts hoch können
        if(getX() % bomberWorld.getGridSize() < bomberWorld.getGridSize() / 2)
        {
            //nach links
            if(canMoveAtPos(dir,bomberWorld.convertGridToPos(gridXPos - 1),getGridYPosAsPixel()))
            {
                int deltaX = bomberWorld.convertGridToPos(gridXPos - 1) - getX();
                int remainingMovement =  movementSpeed - Math.abs(deltaX);
                if(remainingMovement <= 0)
                {
                    
                    move(MovementDirection.Left,movementSpeed);
                    return;
                }
                else
                {
                    
                    move(MovementDirection.Left,-deltaX);
                    move(dir,remainingMovement);
                    return;
                }
            }
        }
        else
        {
            if(canMoveAtPos(dir,bomberWorld.convertGridToPos(gridXPos + 1),getGridYPosAsPixel()))
            {
                int deltaX = bomberWorld.convertGridToPos(gridXPos + 1) - getX();
                int remainingMovement =  movementSpeed - deltaX;
                if(remainingMovement <= 0)
                {
                    
                    move(MovementDirection.Right,movementSpeed);
                    return;
                }
                else
                {
                   
                    move(MovementDirection.Right,deltaX);
                    move(dir,remainingMovement);
                    return;
                }
            }
        }
    }

    protected void decideMoveX(MovementDirection dir)
    {
        if(canMove(dir) == true)
        {
            
            move(dir);
            return;
        }
        //überprüfe ob wir perfekt in unseren grid hoch können

        if(canMoveAtPos(dir,getGridXPosAsPixel(),getGridYPosAsPixel()))
        {
            int deltaY = getGridYPosAsPixel() - getY();
            int remainingMovement =  movementSpeed - Math.abs(deltaY);
            if(remainingMovement <= 0)
            {

                if(deltaY >= 0)
                {
                    
                    move(MovementDirection.Down,movementSpeed);
                }
                else
                {
                    
                    move(MovementDirection.Up,movementSpeed);
                }

                return;
            }
            else
            {
                
                move(MovementDirection.Down,deltaY);
                
                move(dir,remainingMovement);
                return;
            }
        }
        //Überprüfe ob wir links oder rechts hoch können
        if(getY() % bomberWorld.getGridSize() < bomberWorld.getGridSize() / 2)
        {
            //nach links
            if(canMoveAtPos(dir,getGridXPosAsPixel(),bomberWorld.convertGridToPos(gridYPos - 1) ))
            {
                int deltaY = bomberWorld.convertGridToPos(gridYPos - 1) - getY();
                int remainingMovement =  movementSpeed - Math.abs(deltaY);
                if(remainingMovement <= 0)
                {
                    
                    move(MovementDirection.Up,movementSpeed);
                    return;
                }
                else
                {
                    
                    move(dir,-deltaY);
                    move(MovementDirection.Up,remainingMovement);
                    return;
                }
            }
        }
        else
        {
            if(canMoveAtPos(dir,getGridXPosAsPixel(),bomberWorld.convertGridToPos(gridYPos + 1)))
            {
                int deltaX = bomberWorld.convertGridToPos(gridXPos + 1) - getX();
                int remainingMovement =  movementSpeed - deltaX;
                if(remainingMovement <= 0)
                {
                    
                    move(MovementDirection.Down,movementSpeed);
                    return;
                }
                else
                {
                   
                    move(MovementDirection.Down,deltaX);
                    move(dir,remainingMovement);
                    return;
                }
            }
        }
    }
    
    

    protected void decideMove(MovementDirection newDirection)
    {
        switch(newDirection)
        {
            case Up:
            {

                decideMoveY(newDirection);
            }break;
            case Down:
            {
                 decideMoveY(newDirection);
            }break;
            case Left:
            {
                decideMoveX(newDirection);

            }break;
            case Right:
            {
               decideMoveX(newDirection);
            }break;

        }
    }

    protected void move(MovementDirection newDirection)
    {
        move (newDirection,maxMoveDistance(newDirection));
    }

    /**
     * Method move  Da unsere Kollision pixel genau ist muss man perfekt den Spalt treffen um Richtungen zu wechseln. Da dies sehr umständlich ist , gibt es eine kluge Bewegungshilfe.
     *
     * @param newDirection In welche Richtung soll gelaufen werden.
     */
    protected void move(MovementDirection newDirection,int distance)
    {
        if(canMove(newDirection) == false) return;
        if(currentDirection != newDirection)
        {
            currentDirection = newDirection;
            setDirectionImage(newDirection);
        }
        switch(newDirection)
        {
            case Up:
            {

                setLocation(getX(),getY() - distance);
            }break;
            case Down:
            {
                setLocation(getX(),getY() + distance);
            }break;
            case Left:
            {
                setLocation(getX()- distance ,getY());
            }break;
            case Right:
            {
                setLocation(getX() + distance,getY() );
            }break;

        }
    }

    protected void OnReceiveExplosion()
    {
        if(isInvulnerable == false)
        {
             setLifes(getLifes() - 1 );
             OnStartInvulnerability();
        }
        
    }

    /**
     * Method dropBomb
     *
     */
    void dropBomb()
    {
        List<Bomb> bombs = bomberWorld.getObjects(Bomb.class);
        for(Bomb bomb : bombs)
        {
            if(bomb.getGridXPos() == getGridXPos() && bomb.getGridYPos() == getGridYPos())
                return;
        }
        if(getMaxNumOfBombs() > thrownBombsList.size())
        {
            Bomb newBomb = new Bomb(templateBomb);
            bomberWorld.addObject(newBomb,bomberWorld.convertGridToPos(gridXPos),bomberWorld.convertGridToPos(gridYPos));         
            addThrownBomb(newBomb);
        }
        
    }

    /**
     * Method findPowerUp
     *
     */
    void findPowerUp()
    {
        List<PowerUp> foundPowerUpsList = getIntersectingObjects(PowerUp.class);
        for(PowerUp foundPowerUp : foundPowerUpsList)
        {
            switch(foundPowerUp.getPowerUpType())
            {
                case Speed:
                {
                    setMovementSpeed(getMovementSpeed() + foundPowerUp.getValue());
                }break;
                case Death:
                {
                    setLifes(getLifes() + foundPowerUp.getValue());
                }break;

                case Ammount:
                {
                    setMaxNumOfBombs(getMaxNumOfBombs()  + foundPowerUp.getValue());
                }break;
                case Strength:
                {
                    templateBomb.setExplosionGridLength(templateBomb.getExplosionGridLength() + foundPowerUp.getValue());
                }break;
            }
            bomberWorld.removeObject(foundPowerUp);
        }
    }

    protected void loadImage()
    {
        setDirectionImage(currentDirection);
    }
    
    public void OnDeath()
    {
        
    }
}
