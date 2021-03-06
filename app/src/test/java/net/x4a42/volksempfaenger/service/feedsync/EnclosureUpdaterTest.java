package net.x4a42.volksempfaenger.service.feedsync;

import net.x4a42.volksempfaenger.data.entity.enclosure.Enclosure;
import net.x4a42.volksempfaenger.data.entity.enclosure.EnclosureDaoWrapper;
import net.x4a42.volksempfaenger.data.entity.episode.Episode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnclosureUpdaterTest
{
    @Mock EnclosureDaoWrapper enclosureDao;
    @Mock Enclosure           enclosure;
    @Mock Episode             episode;
    EnclosureUpdater          enclosureUpdater;

    net.x4a42.volksempfaenger.feedparser.Enclosure feedEnclosure
            = new net.x4a42.volksempfaenger.feedparser.Enclosure();

    @Before
    public void setUp() throws Exception
    {
        feedEnclosure.url  = "my-url";
        feedEnclosure.mime = "audio/mpeg";
        feedEnclosure.size = 100;
        Mockito.when(enclosureDao.insert(episode,
                                         feedEnclosure.url,
                                         feedEnclosure.mime,
                                         feedEnclosure.size))
               .thenReturn(enclosure);
        enclosureUpdater = new EnclosureUpdater(enclosureDao);
    }

    @Test
    public void insert() throws Exception
    {
        enclosureUpdater.insertOrUpdate(episode, feedEnclosure);
        Mockito.verify(enclosureDao).insert(episode,
                                            feedEnclosure.url,
                                            feedEnclosure.mime,
                                            feedEnclosure.size);
    }

    @Test
    public void update() throws Exception
    {
        Mockito.when(enclosureDao.getByUrl(feedEnclosure.url)).thenReturn(enclosure);
        enclosureUpdater.insertOrUpdate(episode, feedEnclosure);
        Mockito.verify(enclosureDao).update(enclosure, feedEnclosure.mime, feedEnclosure.size);
    }
}