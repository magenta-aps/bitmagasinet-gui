package dk.magenta.bitmagasinet.remote;

import java.util.ArrayList;
import java.util.List;

import dk.magenta.bitmagasinet.checksum.FileChecksum;

public class BitrepositoryConnectorStub implements BitrepositoryConnector {

	private List<ThreadStatusObserver> observers;
	private FileChecksum returnFileChecksum;
	private ThreadStatus threadStatus;

	public BitrepositoryConnectorStub(FileChecksum returnFileChecksum, ThreadStatus threadStatus) {
		this.returnFileChecksum = returnFileChecksum;
		this.threadStatus = threadStatus;
		observers = new ArrayList<ThreadStatusObserver>();
	}

	@Override
	public void run() {
		BitrepositoryConnectionResult bitrepositoryConnectionResult = new BitrepositoryConnectionResultImpl(
				threadStatus, returnFileChecksum);
		notifyObservers(bitrepositoryConnectionResult);
	}

	@Override
	public void addObserver(ThreadStatusObserver observer) {
		observers.add(observer);
	}

	private void notifyObservers(BitrepositoryConnectionResult bitrepositoryConnectionResult) {
		for (ThreadStatusObserver observer : observers) {
			observer.update(bitrepositoryConnectionResult);
		}
	}

}
