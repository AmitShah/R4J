package no.stelar7.api.l4j8.basic.constants.types;

import java.util.Optional;
import java.util.stream.Stream;

public enum TournamentSpectatorType implements CodedEnum
{
    NONE,
    LOBBYONLY,
    ALL;
    
    public Optional<TournamentSpectatorType> getFromCode(final String value)
    {
        return Stream.of(TournamentSpectatorType.values()).filter(t -> t.name().equals(value)).findFirst();
    }
    
    @Override
    public String prettyName()
    {
        switch (this)
        {
            case NONE:
                return "None";
            case LOBBYONLY:
                return "Lobby Only";
            case ALL:
                return "All";
            default:
                return "This enum does not have a pretty name";
        }
    }
}
