package no.stelar7.api.r4j.tests.tft;

import no.stelar7.api.r4j.basic.constants.api.*;
import no.stelar7.api.r4j.impl.R4J;
import no.stelar7.api.r4j.impl.tft.TFTMatchAPI;
import no.stelar7.api.r4j.pojo.shared.GAMHSMatch;
import no.stelar7.api.r4j.pojo.lol.summoner.Summoner;
import no.stelar7.api.r4j.pojo.tft.*;
import no.stelar7.api.r4j.tests.SecretFile;
import org.junit.Test;

import java.util.List;

public class TestTFTMatch
{
    final R4J         l4j8 = new R4J(SecretFile.CREDS);
    final TFTMatchAPI api  = l4j8.getTFTAPI().getMatchAPI();
    
    
    @Test
    public void testFetchList()
    {
        List<String> stelar7 = api.getMatchList(ServicePlatform.EUROPE, Summoner.byName(Platform.EUW1, "stelar7").getPUUID(), 20);
        System.out.println();
    }
    
    @Test
    public void testFetchMatch()
    {
        TFTMatch stelar7 = api.getMatch(ServicePlatform.EUROPE, "EUW1_4240052361");
        System.out.println();
    }
    
    @Test
    public void testFetchMatchRAW()
    {
        GAMHSMatch stelar7 = api.getMatchRAW(ServicePlatform.EUROPE, "EUW1_4240052361");
        System.out.println();
    }
    
    @Test
    public void testMatchIterator()
    {
        MatchIterator stelar7 = api.getMatchIterator(ServicePlatform.EUROPE, Summoner.byName(Platform.EUW1, "stelar7").getPUUID(), 20);
        for (TFTMatch m : stelar7)
        {
            System.out.println(m);
        }
        System.out.println();
    }
}
