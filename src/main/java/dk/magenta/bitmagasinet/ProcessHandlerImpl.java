package dk.magenta.bitmagasinet;

import java.util.ArrayList;
import java.util.List;

import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnectionResult;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnector;
import dk.magenta.bitmagasinet.remote.BitrepositoryProgressHandler;
import dk.magenta.bitmagasinet.remote.BitrepositoryProgressHandlerImpl;
import dk.magenta.bitmagasinet.remote.ThreadStatus;
import dk.magenta.bitmagasinet.remote.ThreadStatusObserver;

public class ProcessHandlerImpl implements ProcessHandler, ThreadStatusObserver {

	private List<FileChecksum> remainingFileChecksums;
	private List<FileChecksum> processedFileChecksums;
	private List<ProcessHandlerObserver> observers;
	private BitrepositoryProgressHandler bitrepositoryProgressHandler;
	private BitrepositoryConnector bitrepositoryConnector;
	private boolean processAutomatically;

	public ProcessHandlerImpl(List<FileChecksum> fileChecksums, BitrepositoryConnector bitrepositoryConnector,
			boolean processAutomatically) {
		this.bitrepositoryConnector = bitrepositoryConnector;
		this.processAutomatically = processAutomatically;
		remainingFileChecksums = fileChecksums;
		processedFileChecksums = new ArrayList<FileChecksum>();
		bitrepositoryProgressHandler = new BitrepositoryProgressHandlerImpl(fileChecksums);
		observers = new ArrayList<ProcessHandlerObserver>();
	}

	@Override
	public void processNext() {
		Thread t = new Thread(bitrepositoryConnector);
		t.start();
	}

	@Override
	public List<FileChecksum> getRemainingFileChecksums() {
		return remainingFileChecksums;
	}

	@Override
	public List<FileChecksum> getProcessedFileChecksums() {
		return processedFileChecksums;
	}

	@Override
	public void update(BitrepositoryConnectionResult bitrepositoryConnectionResult) {
		remainingFileChecksums.remove(0);
		processedFileChecksums.add(bitrepositoryConnectionResult.getFileChecksum());
		bitrepositoryProgressHandler.fileCompleted();
		if (!remainingFileChecksums.isEmpty()) {
			bitrepositoryConnector.setFileChecksum(remainingFileChecksums.get(0));
			if (processAutomatically) {
				processNext();
			}
		} else {
			notifyObservers();
		}
	}

	@Override
	public BitrepositoryProgressHandler getProgressHandler() {
		return bitrepositoryProgressHandler;
	}

	@Override
	public void messageBusErrorCallback() {
		// TODO Auto-generated method stub
	}

	@Override
	public void addObserver(ProcessHandlerObserver processHandlerObserver) {
		observers.add(processHandlerObserver);
	}

	@Override
	public void notifyObservers() {
		if (!observers.isEmpty()) {
			for (ProcessHandlerObserver observer : observers) {
				observer.update(this);
			}
		}
	}

}
