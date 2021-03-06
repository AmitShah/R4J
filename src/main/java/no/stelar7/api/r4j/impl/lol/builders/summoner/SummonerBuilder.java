package no.stelar7.api.r4j.impl.lol.builders.summoner;

import no.stelar7.api.r4j.basic.calling.*;
import no.stelar7.api.r4j.basic.constants.api.*;
import no.stelar7.api.r4j.basic.utils.*;
import no.stelar7.api.r4j.pojo.lol.summoner.Summoner;

import java.util.*;

public class SummonerBuilder
{
    
    private final String   name;
    private final String   sumId;
    private final String   accId;
    private final String   puuid;
    private final Platform platform;
    
    public SummonerBuilder()
    {
        this.name = "";
        this.sumId = "";
        this.accId = "";
        this.puuid = "";
        this.platform = Platform.UNKNOWN;
    }
    
    private SummonerBuilder(String name, String sumId, String accId, String puuid, Platform platform)
    {
        this.name = name;
        this.sumId = sumId;
        this.accId = accId;
        this.puuid = puuid;
        this.platform = platform;
    }
    
    public SummonerBuilder withSummonerId(String id)
    {
        return new SummonerBuilder("", id, "", "", this.platform);
    }
    
    public SummonerBuilder withAccountId(String id)
    {
        return new SummonerBuilder("", "", id, "", this.platform);
    }
    
    public SummonerBuilder withName(String name)
    {
        return new SummonerBuilder(name, "", "", "", this.platform);
    }
    
    public SummonerBuilder withPUUID(String puuid)
    {
        return new SummonerBuilder(name, "", "", puuid, this.platform);
    }
    
    public SummonerBuilder withPlatform(Platform platform)
    {
        return new SummonerBuilder(this.name, this.sumId, this.accId, this.puuid, platform);
    }
    
    /**
     * gets a summoner based on the parameters passed to the builder
     *
     * @return Summoner
     */
    public Summoner get()
    {
        DataCallBuilder builder  = new DataCallBuilder().withPlatform(this.platform);
        URLEndpoint     endpoint = null;
        
        
        Map<String, Object> data = new TreeMap<>();
        data.put("platform", platform);
        
        if (accId.length() > 0)
        {
            builder.withURLParameter(Constants.ACCOUNT_ID_PLACEHOLDER, this.accId);
            endpoint = URLEndpoint.V4_SUMMONER_BY_ACCOUNT;
            data.put("accountid", this.accId);
        }
        
        if (puuid.length() > 0)
        {
            builder.withURLParameter(Constants.PUUID_ID_PLACEHOLDER, this.puuid);
            endpoint = URLEndpoint.V4_SUMMONER_BY_PUUID;
            data.put("puuid", this.puuid);
        }
        
        if (sumId.length() > 0)
        {
            builder.withURLParameter(Constants.SUMMONER_ID_PLACEHOLDER, this.sumId);
            endpoint = URLEndpoint.V4_SUMMONER_BY_ID;
            data.put("id", this.sumId);
        }
        
        if (name.length() > 0)
        {
            builder.withURLParameter(Constants.SUMMONER_NAME_PLACEHOLDER, Utils.normalizeString(this.name));
            endpoint = URLEndpoint.V4_SUMMONER_BY_NAME;
            data.put("name", this.name);
        }
        
        builder.withEndpoint(endpoint);
        
        Optional<?> chl = DataCall.getCacheProvider().get(endpoint, data);
        if (chl.isPresent())
        {
            return (Summoner) chl.get();
        }
        
        
        Object sob = builder.build();
        
        if (sob instanceof Pair)
        {
            return null;
        }
        
        data.put("value", sob);
        DataCall.getCacheProvider().store(endpoint, data);
        
        return (Summoner) sob;
    }
}
