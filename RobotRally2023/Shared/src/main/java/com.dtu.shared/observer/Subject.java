/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.dtu.shared.observer;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * This is the subject of the observer design pattern roughly following
 * the definition of the GoF.
 * 
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public abstract class Subject {

	// The observers are maintained in a weak set to help them unregister from subjects to avoid scrambling the view
	public Set<Observer> observers =
			Collections.newSetFromMap(new WeakHashMap<Observer, Boolean>());

	// Attaches an observer to this subject
	final public void attach(Observer observer) {
		observers.add(observer);
	}

	// Detaches an observer from this subject
	final public void detach(Observer observer) {
		observers.remove(observer);
	}

	// Updates all observers of this subject
	final protected void notifyChange() {
		for (Observer observer: observers) {
			observer.update(this);
		}
	}
}
