package no.stelar7.api.l4j8.impl.builders.championmastery;

import no.stelar7.api.l4j8.basic.calling.*;
import no.stelar7.api.l4j8.basic.constants.api.*;
import no.stelar7.api.l4j8.pojo.championmastery.ChampionMastery;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ChampionMasteryBuilder
{
    
    private final Platform platform;
    private final Long     summonerId;
    private final Integer  championId;
    
    private ChampionMasteryBuilder(Platform platform, Long summonerId, Integer championId)
    {
        this.platform = platform;
        this.summonerId = summonerId;
        this.championId = championId;
    }
    
    public ChampionMasteryBuilder()
    {
        this.platform = Platform.UNKNOWN;
        this.summonerId = null;
        this.championId = null;
    }
    
    
    public ChampionMasteryBuilder withSummonerId(Long id)
    {
        return new ChampionMasteryBuilder(this.platform, id, this.championId);
    }
    
    public ChampionMasteryBuilder withChampionId(Integer id)
    {
        return new ChampionMasteryBuilder(this.platform, this.summonerId, id);
    }
    
    public ChampionMasteryBuilder withPlatform(Platform platform)
    {
        return new ChampionMasteryBuilder(platform, this.summonerId, this.championId);
    }
    
    public Integer getMasteryScore()
    {
        if (this.summonerId == null || this.platform == Platform.UNKNOWN)
        {
            return null;
        }
        
        
        DataCallBuilder builder = new DataCallBuilder().withURLParameter(Constants.SUMMONER_ID_PLACEHOLDER, String.valueOf(this.summonerId))
                                                       .withEndpoint(URLEndpoint.V3_MASTERY_SCORE)
                                                       .withPlatform(this.platform);
        
        
        Optional chl = DataCall.getCacheProvider().get(URLEndpoint.V3_MASTERY_SCORE, this.platform, this.summonerId);
        if (chl.isPresent())
        {
            return (Integer) chl.get();
        }
        
        Integer list = (Integer) builder.build();
        DataCall.getCacheProvider().store(URLEndpoint.V3_MASTERY_SCORE, list, this.platform, this.summonerId);
        return list;
    }
    
    public List<ChampionMastery> getChampionMasteries()
    {
        if (this.summonerId == null || this.platform == Platform.UNKNOWN)
        {
            return null;
        }
        
        DataCallBuilder builder = new DataCallBuilder().withURLParameter(Constants.SUMMONER_ID_PLACEHOLDER, String.valueOf(this.summonerId))
                                                       .withEndpoint(URLEndpoint.V3_MASTERY_BY_ID)
                                                       .withPlatform(this.platform);
        
        
        Optional chl = DataCall.getCacheProvider().get(URLEndpoint.V3_MASTERY_BY_ID, this.platform, this.summonerId);
        if (chl.isPresent())
        {
            return (List<ChampionMastery>) chl.get();
        }
        
        List<ChampionMastery> list = (List<ChampionMastery>) builder.build();
        DataCall.getCacheProvider().store(URLEndpoint.V3_MASTERY_BY_ID, list, this.platform, this.summonerId);
        
        return list;
    }
    
    public List<ChampionMastery> getTopChampions(Integer count)
    {
        List<ChampionMastery> list = getChampionMasteries();
        
        return list.stream().sorted(Comparator.comparing(ChampionMastery::getChampionPoints))
                   .limit(count != null ? count : 3)
                   .collect(Collectors.toList());
        
    }
    
    
    public ChampionMastery getChampionMastery()
    {
        if (this.championId == null || this.summonerId == null || this.platform == Platform.UNKNOWN)
        {
            return null;
        }
        
        DataCallBuilder builder = new DataCallBuilder().withURLParameter(Constants.SUMMONER_ID_PLACEHOLDER, String.valueOf(this.summonerId))
                                                       .withURLParameter(Constants.CHAMPION_ID_PLACEHOLDER, String.valueOf(this.championId))
                                                       .withEndpoint(URLEndpoint.V3_MASTERY_BY_CHAMPION)
                                                       .withPlatform(this.platform);
        
        
        Optional chl = DataCall.getCacheProvider().get(URLEndpoint.V3_MASTERY_BY_CHAMPION, this.platform, this.summonerId, this.championId);
        if (chl.isPresent())
        {
            return (ChampionMastery) chl.get();
        }
        
        ChampionMastery mastery = (ChampionMastery) builder.build();
        
        if (mastery == null)
        {
            try
            {
                mastery = new ChampionMastery();
                
                Field player = mastery.getClass().getDeclaredField("playerId");
                player.setAccessible(true);
                player.set(mastery, this.summonerId);
                
                Field champ = mastery.getClass().getDeclaredField("championId");
                champ.setAccessible(true);
                champ.set(mastery, this.championId);
                
                Field level = mastery.getClass().getDeclaredField("championLevel");
                level.setAccessible(true);
                level.set(mastery, 0);
            } catch (NoSuchFieldException | IllegalAccessException e)
            {
                Logger.getGlobal().warning("Class has changed, please fix me");
            }
        }
        
        DataCall.getCacheProvider().store(URLEndpoint.V3_MASTERY_BY_CHAMPION, mastery, this.platform, this.summonerId, this.championId);
        return mastery;
    }
}
