package hust.tools.hmm.learn;

import java.io.File;
import java.util.HashMap;

import hust.tools.hmm.model.ARPAEntry;
import hust.tools.hmm.model.EmissionProbEntry;
import hust.tools.hmm.model.HMMModel;
import hust.tools.hmm.model.TransitionProbEntry;
import hust.tools.hmm.utils.State;
import hust.tools.hmm.utils.StateSequence;

/**
 *<ul>
 *<li>Description: 顶层HMM模型训练学习抽象类，被AbstractSupervisedLearner与AbstractUnsupervisedLearner继承分别实现基于监督和非监督的训练方法 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2018年1月10日
 *</ul>
 */
public class AbstractLearner {
	
	/**
	 * 默认HMM模型阶数
	 */
	protected final int DEFAULT_ORDER = 1;
	
	protected int order;
		
	protected HashMap<State, ARPAEntry> pi;
	
	protected HashMap<StateSequence, TransitionProbEntry> transitionMatrix;
	
	protected HashMap<State, EmissionProbEntry> emissionMatrix;
	
	public AbstractLearner() {
		this.order = DEFAULT_ORDER;
		pi = new HashMap<>();
		transitionMatrix = new HashMap<>();
		emissionMatrix = new HashMap<>();
	}
	
	public AbstractLearner(int order) {
		this.order = order;
		pi = new HashMap<>();
		transitionMatrix = new HashMap<>();
		emissionMatrix = new HashMap<>();
	}
	
	/**
	 * 加载模型文件
	 * @param modelFile	模型文件
	 * @return			HMM模型
	 */
	public HMMModel loadModel(File modelFile) {
		
		return null;
	}
	
	/**
	 * 写出模型
	 * @param model		HMM模型
	 * @param modelFile	写出路径
	 */
	public void writeModel(HMMModel model, File modelFile) {
		
	}
}
