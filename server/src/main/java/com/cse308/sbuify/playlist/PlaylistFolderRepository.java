package com.cse308.sbuify.playlist;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cse308.sbuify.user.User;

public interface PlaylistFolderRepository extends CrudRepository<PlaylistFolder, Integer> {
    List<PlaylistFolder> findAllByOwner_Id(Integer ownerId);
}
