package hust.tools.hmm.learn;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import hust.tools.hmm.model.Emission;
import hust.tools.hmm.model.Transition;
import hust.tools.hmm.pos.StringObservation;
import hust.tools.hmm.pos.StringState;
import hust.tools.hmm.stream.SupervisedHMMSample;
import hust.tools.hmm.utils.Observation;
import hust.tools.hmm.utils.ObservationSequence;
import hust.tools.hmm.utils.State;
import hust.tools.hmm.utils.StateSequence;

/**
 *<ul>
 *<li>Description: 转移和发射计数类的单元测试
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2018年1月9日
 *</ul>
 */
public class TransitionAndEmissionCounterTest {
	
	private short order;
	private short cutoff;
	private List<SupervisedHMMSample> samples;
	private TransitionAndEmissionCounter counter;

	private StateSequence sSequence;
	private ObservationSequence oSequence;
	private SupervisedHMMSample sample;
	
	
	private State[] states23;
	private State[] states89;
	private State[] states36;
	
	 
	@Before
	public void setUp() throws Exception {
		states23 = new StringState[]{new StringState("2"), new StringState("3")};
		states36 = new StringState[]{new StringState("3"), new StringState("6")};
		states89 = new StringState[]{new StringState("8"), new StringState("9")};
		
		String[] h = new String[]{"1", "3", "4", "6", "8", "9", "0"};
		String[] o = new String[]{"c", "b", "b", "a", "e", "a", "b"};
		State[] ss = new StringState[h.length];
		Observation[] os = new StringObservation[o.length];
		for(int i = 0; i < h.length; i++) {
			ss[i] = new StringState(h[i]);
			os[i] = new StringObservation(o[i]);
		}
		sSequence = new StateSequence(ss);
		oSequence = new ObservationSequence(os);
		sample = new SupervisedHMMSample(sSequence, oSequence);
		
		
		order = 3;
		cutoff = 0;
		samples = new ArrayList<>();
		StateSequence stateSequence = null;
		ObservationSequence observationSequence = null;
		
		String[] h1 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
		String[] o1 = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
		State[] states = new StringState[h1.length];
		Observation[] observations = new StringObservation[o1.length];
		for(int i = 0; i < h1.length; i++) {
			states[i] = new StringState(h1[i]);
			observations[i] = new StringObservation(o1[i]);
		}
		stateSequence = new StateSequence(states);
		observationSequence = new ObservationSequence(observations);
		samples.add(new SupervisedHMMSample(stateSequence, observationSequence));
		
		String[] h2 = new String[]{"1", "3", "5", "7", "9"};
		String[] o2 = new String[]{"e", "c", "a", "h", "j"};
		states = new StringState[h2.length];
		observations = new StringObservation[o2.length];
		for(int i = 0; i < h2.length; i++) {
			states[i] = new StringState(h2[i]);
			observations[i] = new StringObservation(o2[i]);
		}
		stateSequence = new StateSequence(states);
		observationSequence = new ObservationSequence(observations);
		samples.add(new SupervisedHMMSample(stateSequence, observationSequence));
		
		String[] h3 = new String[]{"0", "8", "6", "4", "2"};
		String[] o3 = new String[]{"b", "e", "h", "f", "d"};
		states = new StringState[h3.length];
		observations = new StringObservation[o3.length];
		for(int i = 0; i < h3.length; i++) {
			states[i] = new StringState(h3[i]);
			observations[i] = new StringObservation(o3[i]);
		}
		stateSequence = new StateSequence(states);
		observationSequence = new ObservationSequence(observations);
		samples.add(new SupervisedHMMSample(stateSequence, observationSequence));
		
		counter = new TransitionAndEmissionCounter(samples, order, cutoff);
	}

	@Test
	public void testUpdate() {
		counter.update(sample, order);
		
		State[] states = new StringState[]{new StringState("8"), new StringState("9"), new StringState("0")};
		assertEquals(1, counter.getSequeceCount(new StateSequence(states)));
		assertEquals(1, counter.getSequeceCount(new StateSequence(states23)));
		assertEquals(3, counter.getSequeceCount(new StateSequence(new StringState("3"))));
		
		
		State target = new StringState("4");
		Transition transition = new Transition(states23, target);
		assertEquals(1, counter.getTransitionCount(transition));
		
		states = new StringState[]{new StringState("3")};
		transition = new Transition(states, target);
		assertEquals(2, counter.getTransitionCount(transition));
		
		
		
		State state = new StringState("8");
		Observation observation = new StringObservation("e");
		assertEquals(2, counter.getEmissionCount(new Emission(state, observation)));
		
		state = new StringState("5");
		observation = new StringObservation("z");
		assertEquals(0, counter.getEmissionCount(new Emission(state, observation)));
	}

	@Test
	public void testGetSequeceCount() {
		State[] states = new StringState[]{new StringState("8"), new StringState("9"), new StringState("0")};
		assertEquals(0, counter.getSequeceCount(new StateSequence(states)));
		states = new StringState[]{new StringState("7"), new StringState("8"), new StringState("9"), new StringState("0")};
		assertEquals(0, counter.getSequeceCount(new StateSequence(states)));
		
		assertEquals(1, counter.getSequeceCount(new StateSequence(states23)));
		assertEquals(0, counter.getSequeceCount(new StateSequence(states36)));
		assertEquals(2, counter.getSequeceCount(new StateSequence(new StringState("3"))));
	}

	@Test
	public void testGetTransitionCount() {
		State target = new StringState("4");
		Transition transition = new Transition(states23, target);
		assertEquals(1, counter.getTransitionCount(transition));
		
		transition = new Transition(states89, target);
		assertEquals(0, counter.getTransitionCount(transition));
		
		State[] states = new StringState[]{new StringState("3")};
		transition = new Transition(states, target);
		assertEquals(1, counter.getTransitionCount(transition));
		
		target = new StringState("7");
		transition = new Transition(states36, target);
		assertEquals(0, counter.getTransitionCount(transition));
	}

	@Test
	public void testGetEmissionCount() {
		State state = new StringState("8");
		Observation observation = new StringObservation("e");
		assertEquals(1, counter.getEmissionCount(new Emission(state, observation)));
		
		state = new StringState("5");
		observation = new StringObservation("z");
		assertEquals(0, counter.getEmissionCount(new Emission(state, observation)));
		
		observation = new StringObservation("c");
		assertEquals(0, counter.getEmissionCount(new Emission(state, observation)));
		
		state = new StringState("12");
		assertEquals(0, counter.getEmissionCount(new Emission(state, observation)));
		
		state = new StringState("3");
		assertEquals(2, counter.getEmissionCount(new Emission(state, observation)));
	}

	@Test
	public void testContainStateSequence() {
		assertTrue(counter.contain(new StateSequence(states23)));
		assertTrue(counter.contain(new StateSequence(states89)));
		assertFalse(counter.contain(new StateSequence(states36)));
	}

	@Test
	public void testContainState() {
		State state = new StringState("9");
		assertTrue(counter.contain(state));
		
		state = new StringState("2");
		assertTrue(counter.contain(state));
		
		state = new StringState("12");
		assertFalse(counter.contain(state));
	}

	@Test
	public void testContainEmission() {
		State state = new StringState("9");
		Observation observation = new StringObservation("i");
		
		assertTrue(counter.contain(new Emission(state, observation)));
		
		state = new StringState("5");
		observation = new StringObservation("c");
		assertFalse(counter.contain(new Emission(state, observation)));
		
		state = new StringState("12");
		observation = new StringObservation("c");
		assertFalse(counter.contain(new Emission(state, observation)));
		
		state = new StringState("5");
		observation = new StringObservation("z");
		assertFalse(counter.contain(new Emission(state, observation)));
	}

	@Test
	public void testContainTransition() {
		State target = new StringState("4");
		Transition transition = new Transition(states23, target);
		assertTrue(counter.contain(transition));
		
		transition = new Transition(states89, target);
		assertFalse(counter.contain(transition));
		
		target = new StringState("7");
		transition = new Transition(states36, target);
		assertFalse(counter.contain(transition));
	}

	@Test
	public void testCutoff() {
		counter.cutoff(2);

		State state = new StringState("9");
		Observation observation = new StringObservation("i");
		assertFalse(counter.contain(new Emission(state, observation)));
		
		state = new StringState("3");
		observation = new StringObservation("c");
		assertEquals(2, counter.getEmissionCount(new Emission(state, observation)));
	}
}