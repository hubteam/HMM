package hust.tools.hmm.model;

import hust.tools.hmm.utils.StateSequence;
import hust.tools.hmm.utils.StringObservation;

import java.util.HashMap;
import java.util.Iterator;
import hust.tools.hmm.utils.Dictionary;
import hust.tools.hmm.utils.Observation;
import hust.tools.hmm.utils.ObservationSequence;
import hust.tools.hmm.utils.State;

/**
 *<ul>
 *<li>Description: 基于回退的隐式马尔科夫模型 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年12月26日
 *</ul>
 */
public class HMModelBasedBOW implements HMModel {
	
	private final Observation UNKNOWN = new StringObservation("UNKNOWN");
	
	private int order;
	
	private Dictionary dict;
	
	private HashMap<State, ARPAEntry> pi;
	
	private HashMap<StateSequence, TransitionProbEntry>  transitionMatrix;
	
	private HashMap<State, EmissionProbEntry>  emissionMatrix;
	
	public HMModelBasedBOW(int order, Dictionary dict, HashMap<State, ARPAEntry> pi, HashMap<StateSequence, TransitionProbEntry>  transitionMatrix, HashMap<State, EmissionProbEntry>  emissionMatrix) {
		this.order = order;
		this.dict = dict;
		this.pi = pi;
		this.transitionMatrix = transitionMatrix;
		this.emissionMatrix = emissionMatrix;
	}
	
	public int getOrder() {
		return order;
	}
	
	public Dictionary getDict() {
		return dict;
	} 
	
	public HashMap<State, ARPAEntry> getPi() {
		return pi;
	}

	public HashMap<StateSequence, TransitionProbEntry> getTransitionMatrix() {
		return transitionMatrix;
	}

	public HashMap<State, EmissionProbEntry> getEmissionMatrix() {
		return emissionMatrix;
	}
	
	@Override
	public double transitionProb(StateSequence start, State target) {
		if(contain(start, target))
			return transitionMatrix.get(start).getTransitionLogProb(target);
		
		return transitionBow(start, target);
	}
	
	private double transitionBow(StateSequence start, State target) {
		return 0;
	}
	
	public double transitionProb(int[] start, int target) {
		for(int index : start)
			if(!dict.containState(index))
				throw new IllegalArgumentException("不存在的状态索引：" + index);
		if(!dict.containState(target))
			throw new IllegalArgumentException("不存在的状态索引：" + target);

		State[] states = new State[start.length];
		for(int i = 0; i < start.length; i++)
			states[i] = dict.getState(start[i]);
		
		State state = dict.getState(target);
		
		return transitionProb(new StateSequence(states), state);
	}

	@Override
	public double emissionProb(State state, Observation observation) {
		if(!dict.containState(state))
			throw new IllegalArgumentException("不存在的隐藏状态:" + state);
		
		if(contain(state, observation))
			return emissionMatrix.get(state).getEmissionLogProb(observation);
		
		return emissionMatrix.get(state).getEmissionLogProb(UNKNOWN);
	}
	
	public double emissionProb(int state, int observation) {
		if(!dict.containState(state))
			throw new IllegalArgumentException("不存在的状态索引：" + state);
		else if(!dict.containObservation(observation))
			throw new IllegalArgumentException("不存在的观测索引：" + observation);
		
		State si = dict.getState(state);
		Observation ot = dict.getObservation(observation);
		
		return emissionProb(si, ot);
	}

	@Override
	public Observation[] getObservations() {
		Iterator<Observation> iterator = dict.observationsIterator();
		Observation[] observations = new Observation[dict.observationCount()];
		int i = 0;
		while(iterator.hasNext()) {
			observations[i++] = iterator.next();
		}
		
		return observations;
	}

	@Override
	public State[] getStates() {
		Iterator<State> iterator = dict.statesIterator();
		State[] states = new State[dict.stateCount()];
		int i = 0;
		while(iterator.hasNext()) {
			states[i++] = iterator.next();
		}
		
		return states;
	}

	@Override
	public double getProb(ObservationSequence observations, StateSequence states, int order) {
		return 0;
	}

	@Override
	public double getPi(State state) {		
		return pi.get(state).getLog_prob();
	}
	
	public double getPi(int i) {
		return getPi(dict.getState(i));
	}
	
	public Observation getObservation(int i) {
		return dict.getObservation(i);
	}
	
	public int getObservationIndex(Observation observation) {
		return dict.getIndex(observation);
	}
	
	public State getState(int i) {
		return dict.getState(i);
	}
	
	public int getStateIndex(State state) {
		return dict.getIndex(state);
	}
	
	public int getStateCount() {
		return dict.stateCount();
	}
	
	public int getObservationCount() {
		return dict.observationCount();
	}
	
	/**
	 * 判断是否包含给定转移
	 * @param start		转移的的起点
	 * @param target	转移的的目标状态
	 * @return			true-包含/false-不包含
	 */
	private boolean contain(StateSequence start, State target) {
		if(transitionMatrix.containsKey(start))
			if(transitionMatrix.get(start).contain(target))
				return true;
		
		return false;
	}
	
	/**
	 * 判断是否包含给定发射
	 * @param state			发射的隐藏状态
	 * @param observation	发射的观测状态
	 * @return				true-包含/false-不包含
	 */
	private boolean contain(State state, Observation observation) {
		if(emissionMatrix.containsKey(state))
			if(emissionMatrix.get(state).contain(observation))
				return true;
		
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dict == null) ? 0 : dict.hashCode());
		result = prime * result + ((emissionMatrix == null) ? 0 : emissionMatrix.hashCode());
		result = prime * result + order;
		result = prime * result + ((pi == null) ? 0 : pi.hashCode());
		result = prime * result + ((transitionMatrix == null) ? 0 : transitionMatrix.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HMModelBasedBOW other = (HMModelBasedBOW) obj;
		if (dict == null) {
			if (other.dict != null)
				return false;
		} else if (!dict.equals(other.dict))
			return false;
		if (emissionMatrix == null) {
			if (other.emissionMatrix != null)
				return false;
		} else if (!emissionMatrix.equals(other.emissionMatrix))
			return false;
		if (order != other.order)
			return false;
		if (pi == null) {
			if (other.pi != null)
				return false;
		} else if (!pi.equals(other.pi))
			return false;
		if (transitionMatrix == null) {
			if (other.transitionMatrix != null)
				return false;
		} else if (!transitionMatrix.equals(other.transitionMatrix))
			return false;
		return true;
	}
	
	
}