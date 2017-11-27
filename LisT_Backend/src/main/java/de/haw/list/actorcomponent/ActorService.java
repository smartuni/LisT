/**
 * 
 */
package de.haw.list.actorcomponent;

import de.haw.list.actorcomponent.dto.ActorMqttDto;

/**
 * Service fuer Aktoren.
 * @author Lydia Pflug
 * 20.11.2017
 */
public interface ActorService {

	/**
	 * Setzt neuen Aktor.
	 * @param actorDto
	 */
	public void setActor(ActorMqttDto actorDto);

}
