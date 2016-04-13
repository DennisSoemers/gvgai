package controllers.sampleOLMCTS;

import java.util.ArrayList;
import java.util.Random;

import ontology.Types;
import tools.ElapsedCpuTimer;
import core.game.StateObservation;
import core.player.AbstractPlayer;

/**
 * Created with IntelliJ IDEA.
 * User: ssamot
 * Date: 14/11/13
 * Time: 21:45
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class Agent extends AbstractPlayer {

    public static int NUM_ACTIONS;
    public static int MCTS_ITERATIONS = 100;
    public static int ROLLOUT_DEPTH = 10;
    public static double K = Math.sqrt(2);
    public static double REWARD_DISCOUNT = 1.00;
    public static Types.ACTIONS[] actions;

    protected SingleMCTSPlayer mctsPlayer;
    
    public static int NUM_TICKS = 0;
    public static int NUM_ITERATIONS = 0;

    /**
     * Public constructor with state observation and time due.
     * @param so state observation of the current game.
     * @param elapsedTimer Timer for the controller creation.
     */
    public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer)
    {
        //Get the actions in a static array.
        ArrayList<Types.ACTIONS> act = so.getAvailableActions();
        actions = new Types.ACTIONS[act.size()];
        for(int i = 0; i < actions.length; ++i)
        {
            actions[i] = act.get(i);
        }
        NUM_ACTIONS = actions.length;

        //Create the player.

        mctsPlayer = getPlayer(so, elapsedTimer);
    }

    public SingleMCTSPlayer getPlayer(StateObservation so, ElapsedCpuTimer elapsedTimer) {
        return new SingleMCTSPlayer(new Random());
    }


    /**
     * Picks an action. This function is called every game step to request an
     * action from the player.
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
     */
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
    	
    	++NUM_TICKS;

        //Set the state observation object as the new root of the tree.
        mctsPlayer.init(stateObs);

        //Determine the action using MCTS...
        int action = mctsPlayer.run(elapsedTimer);

        //... and return it.
        return actions[action];
    }
    
    public void result(StateObservation stateObservation, ElapsedCpuTimer elapsedCpuTimer)
    {
    	System.out.println("Average num MCTS iterations = " + ((double)NUM_ITERATIONS) / NUM_TICKS);
    }

}
