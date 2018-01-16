package hust.tools.hmm.io;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import hust.tools.hmm.model.ARPAEntry;
import hust.tools.hmm.utils.Observation;
import hust.tools.hmm.utils.State;
import hust.tools.hmm.utils.StateSequence;
import hust.tools.hmm.utils.StringObservation;
import hust.tools.hmm.utils.StringState;

/**
 *<ul>
 *<li>Description: 读取二进制数据
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2018年1月14日
 *</ul>
 */
public class BinaryDataReader implements DataReader {
	
	private DataInputStream dis;

	public BinaryDataReader(File file) throws IOException {
		dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
	}

	public BinaryDataReader(InputStream in) {
		dis = new DataInputStream(in);
	}

	public BinaryDataReader(DataInputStream dis) {
		this.dis = dis;
	}

	@Override
	public long readCount() throws IOException {
		return dis.readLong();
	}

	@Override
	public ObservationIndex readObservationIndex() throws IOException {
		String line = dis.readUTF();
		String[] items = line.split("\t");
		
		return new ObservationIndex(new StringObservation(items[0]), Integer.parseInt(items[1]));
	}
	
	@Override
	public StateIndex readStateIndex() throws IOException {
		String line = dis.readUTF();
		String[] items = line.split("\t");
		
		return new StateIndex(new StringState(items[0]), Integer.parseInt(items[1]));
	}

	@Override
	public PiEntry readPi() throws IOException {
		String line = dis.readUTF();
		String[] items = line.split("\t");
		
		return new PiEntry(new StringState(items[0]), Double.parseDouble(items[1]));
	}

	@Override
	public TransitionEntry readTransitionMatrix() throws IOException {
		String line = dis.readUTF();
		String[] items = line.split("\t");
		
		String[] stateArray = items[0].split(" ");
		State[] states = new StringState[stateArray.length];
		for(int i = 0; i < states.length; i++)
			states[i] = new StringState(stateArray[i]);
					
		String[] arpa = items[1].split(" ");
		ARPAEntry entry = new ARPAEntry(Double.parseDouble(arpa[0]), Double.parseDouble(arpa[1]));
		
		return new TransitionEntry(new StateSequence(states), entry);
	}

	@Override
	public EmissionEntry readEmissionMatrix() throws IOException {
		String line = dis.readUTF();
		String[] items = line.split("\t");
		
		State state = new StringState(items[0]);
		Observation observation = new StringObservation(items[1]);
		double logProb = Double.parseDouble(items[2]);
		
		return new EmissionEntry(state, observation, logProb);
	}

	@Override
	public void close() throws IOException {
		dis.close();
	}
}
