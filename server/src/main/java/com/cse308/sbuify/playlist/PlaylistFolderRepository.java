package com.cse308.sbuify.playlist;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PlaylistFolderRepository extends CrudRepository<PlaylistFolder, Integer> {

    List<PlaylistFolder> removeByParent(PlaylistFolder tgt);

}
