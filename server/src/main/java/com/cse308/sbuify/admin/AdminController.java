package com.cse308.sbuify.admin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.common.api.DecorateResponse;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;

@Controller
@RequestMapping(path = "/api/admins/")
public class AdminController {
    private static final String QUERY_START_THIS_MONTH = ""
            + "SELECT COUNT(*) "
            + "FROM Customer C, Subscription S "
            + "WHERE C.subscription_id = S.id "
            + "AND S.start BETWEEN DATE_SUB(Date_Format( now() , '%Y-%m-01'), INTERVAL ? MONTH) AND DATE_SUB(Date_Format( now() , '%Y-%m-01'), INTERVAL ? MONTH);";
    private static final String QUERY_END_THIS_MONTH = ""
            + "SELECT COUNT(*) "
            + "FROM Customer C, Subscription S "
            + "WHERE C.subscription_id = S.id "
            + "AND S.end BETWEEN DATE_SUB(Date_Format( now() , '%Y-%m-01'), INTERVAL ? MONTH) AND DATE_SUB(Date_Format( now() , '%Y-%m-01'), INTERVAL ? MONTH);";
    private static final String QUERY_CURRENT_SUBSCRIBERS = ""
            + "SELECT COUNT(*) "
            + "FROM Customer C, Subscription S "
            + "WHERE C.subscription_id = S.id AND now() BETWEEN S.start AND S.end;";

    private static final String QUERY_MOST_STREAMED_ALLTIME_ALL = ""
            + "SELECT Song.name, COUNT(*) AS Cnt "
            + "FROM Stream S, Song "
            + "WHERE S.song_id = Song.id "
            + "GROUP BY S.song_id "
            + "ORDER BY Cnt DESC "
            + "LIMIT ?;";
    private static final String QUERY_MOST_STREAMED_ALLTIME_PREMIUM = ""
            + "SELECT Song.name, COUNT(*) AS Cnt "
            + "FROM Stream S, Song "
            + "WHERE S.song_id = Song.id AND S.Premium = 1 "
            + "GROUP BY S.song_id "
            + "ORDER BY Cnt DESC "
            + "LIMIT ?;";
    private static final String QUERY_MOST_STREAMED_ALLTIME_NONPREMIUM = ""
            + "SELECT Song.name, COUNT(*) AS Cnt "
            + "FROM Stream S, Song "
            + "WHERE S.song_id = Song.id AND S.Premium = 0 "
            + "GROUP BY S.song_id "
            + "ORDER BY Cnt DESC "
            + "LIMIT ?;";

    private static final String QUERY_MOST_STREAMED_THIS_MONTH_ALL = ""
            + "SELECT Song.name, COUNT(*) AS Cnt "
            + "FROM Stream S, Song "
            + "WHERE S.song_id = Song.id "
            + "AND S.time BETWEEN DATE_SUB(Date_Format( now() , '%Y-%m-01'), INTERVAL 0 MONTH) AND DATE_SUB(Date_Format( now() , '%Y-%m-01'), INTERVAL -1 MONTH) "
            + "GROUP BY S.song_id "
            + "ORDER BY Cnt DESC "
            + "LIMIT ?;";
    private static final String QUERY_MOST_STREAMED_THIS_MONTH_PREMIUM = ""
            + "SELECT Song.name, COUNT(*) AS Cnt "
            + "FROM Stream S, Song "
            + "WHERE S.song_id = Song.id "
            + "AND S.Premium = 1 "
            + "AND S.time BETWEEN DATE_SUB(Date_Format( now() , '%Y-%m-01'), INTERVAL 0 MONTH) AND DATE_SUB(Date_Format( now() , '%Y-%m-01'), INTERVAL -1 MONTH) "
            + "GROUP BY S.song_id "
            + "ORDER BY Cnt DESC "
            + "LIMIT ?;";
    private static final String QUERY_MOST_STREAMED_THIS_MONTH_NONPREMIUM = ""
            + "SELECT Song.name, COUNT(*) AS Cnt "
            + "FROM Stream S, Song "
            + "WHERE S.song_id = Song.id "
            + "AND S.Premium = 0 "
            + "AND S.time BETWEEN DATE_SUB(Date_Format( now() , '%Y-%m-01'), INTERVAL 0 MONTH) AND DATE_SUB(Date_Format( now() , '%Y-%m-01'), INTERVAL -1 MONTH) "
            + "GROUP BY S.song_id "
            + "ORDER BY Cnt DESC "
            + "LIMIT ?;";

    private static final Integer PICK_TOP_N = 3;
    private static final Integer RECENT_N_MONTHS = 6;

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private EntityManager em;

    /**
     * Get admin by Id
     * 
     * @param id
     * @return Http.OK successful, Http.NOT_FOUND not found
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        Admin admin = getAdminById(id);
        if (admin == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    /**
     * get all admins
     * 
     * @return Http.OK successful
     */
    @GetMapping
    public @ResponseBody TypedCollection getAllAdmins() {
        Iterable<Admin> adminIterable = adminRepo.findAll();
        Set<Admin> admins = new HashSet<>();
        adminIterable.forEach(labelOwner -> admins.add(labelOwner));
        return new TypedCollection(admins, Admin.class);
    }

    /**
     * Create an admin
     * 
     * @param admin
     * @return Http.CREATED, successful, Http.FORBIDDEN, no permission,
     *         HTTP.BAD_REQUEST, invalid request body
     */
    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
        if (admin == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!isSuperAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userRepository.save(admin);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Delete an admin given Id
     * 
     * @param id
     * @return Http.OK successful, Http.FORBIDDEN, no permission, Http.NOT_FOUND,
     *         invalid id
     */
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Integer id) {
        Admin admin = getAdminById(id);
        if (admin == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!isSuperAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        userRepository.delete(admin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Update an admin
     * 
     * @param id
     * @param partialAdmin
     * @return Http.OK successful, Http.FORBIDDEN, no permission, Http.NOT_FOUND,
     *         invalid id
     */
    @PatchMapping(path = "{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Integer id, @RequestBody Admin partialAdmin) {
        Admin admin = getAdminById(id);

        if (admin == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!isSuperAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (partialAdmin.isSuperAdmin() != null) {
            admin.setSuperAdmin(partialAdmin.isSuperAdmin());
        }
        if (partialAdmin.getFirstName() != null) {
            admin.setFirstName(partialAdmin.getFirstName());
        }
        if (partialAdmin.getLastName() != null) {
            admin.setLastName(partialAdmin.getLastName());
        }

        userRepository.save(admin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/site-stat")
    @DecorateResponse(type = TypedCollection.class)
    public ResponseEntity<?> getSiteStats() {
        List<String> stats = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        now.getYear();

        Query q = em.createNativeQuery(QUERY_START_THIS_MONTH);
        for (int i = 0; i < RECENT_N_MONTHS; i++) {
            q.setParameter(1, i + 1);
            q.setParameter(2, i);
            LocalDateTime from = now.minusMonths(i + 1);
            LocalDateTime to = now.minusMonths(i);
            stats.add(String.format("Subscription Started Between %d-%02d ~ %d-%02d", from.getYear(),
                    from.getMonth().getValue(), to.getYear(), to.getMonth().getValue()));
            stats.add(q.getResultList().get(0).toString());
        }

        Query q2 = em.createNativeQuery(QUERY_END_THIS_MONTH);
        for (int i = 0; i < RECENT_N_MONTHS; i++) {
            q2.setParameter(1, i + 1);
            q2.setParameter(2, i);
            LocalDateTime from = now.minusMonths(i + 1);
            LocalDateTime to = now.minusMonths(i);
            stats.add(String.format("Subscription Ended Between %d-%02d ~ %d-%02d", from.getYear(),
                    from.getMonth().getValue(), to.getYear(), to.getMonth().getValue()));
            stats.add(q2.getResultList().get(0).toString());
        }

        Query q3 = em.createNativeQuery(QUERY_CURRENT_SUBSCRIBERS);
        stats.add("Current Subscribers");
        stats.add(q3.getResultList().get(0).toString());

        Query q4 = em.createNativeQuery(QUERY_MOST_STREAMED_ALLTIME_ALL);
        q4.setParameter(1, PICK_TOP_N);
        List<Object[]> resultList = q4.getResultList();
        int i = 1;
        for (Object[] obj : resultList) {
            stats.add(String.format("All time most streamed top %d", i++));
            stats.add(String.format("%s: %d", obj[0], obj[1]));
        }

        Query q5 = em.createNativeQuery(QUERY_MOST_STREAMED_ALLTIME_NONPREMIUM);
        q5.setParameter(1, PICK_TOP_N);
        resultList = q5.getResultList();
        i = 1;
        for (Object[] obj : resultList) {
            stats.add(String.format("All time most streamed by non-premium users top %d", i++));
            stats.add(String.format("%s: %d", obj[0], obj[1]));
        }

        Query q6 = em.createNativeQuery(QUERY_MOST_STREAMED_ALLTIME_PREMIUM);
        q6.setParameter(1, PICK_TOP_N);
        resultList = q6.getResultList();
        i = 1;
        for (Object[] obj : resultList) {
            stats.add(String.format("All time most streamed by premium users top %d", i++));
            stats.add(String.format("%s: %d", obj[0], obj[1]));
        }

        Query q7 = em.createNativeQuery(QUERY_MOST_STREAMED_ALLTIME_PREMIUM);
        q7.setParameter(1, PICK_TOP_N);
        resultList = q7.getResultList();
        i = 1;
        for (Object[] obj : resultList) {
            stats.add(String.format("This month's most streamed top %d", i++));
            stats.add(String.format("%s: %d", obj[0], obj[1]));
        }

        Query q8 = em.createNativeQuery(QUERY_MOST_STREAMED_THIS_MONTH_ALL);
        q8.setParameter(1, PICK_TOP_N);
        resultList = q8.getResultList();
        i = 1;
        for (Object[] obj : resultList) {
            stats.add(String.format("This month's most streamed top %d", i++));
            stats.add(String.format("%s: %d", obj[0], obj[1]));
        }

        Query q9 = em.createNativeQuery(QUERY_MOST_STREAMED_THIS_MONTH_PREMIUM);
        q9.setParameter(1, PICK_TOP_N);
        resultList = q9.getResultList();
        i = 1;
        for (Object[] obj : resultList) {
            stats.add(String.format("This month's most streamed by premium users top %d", i++));
            stats.add(String.format("%s: %d", obj[0], obj[1]));
        }

        Query q10 = em.createNativeQuery(QUERY_MOST_STREAMED_THIS_MONTH_NONPREMIUM);
        q10.setParameter(1, PICK_TOP_N);
        resultList = q10.getResultList();
        i = 1;
        for (Object[] obj : resultList) {
            stats.add(String.format("This month's most streamed by non-premium users top %d", i++));
            stats.add(String.format("%s: %d", obj[0], obj[1]));
        }

        return new ResponseEntity<TypedCollection>(new TypedCollection(stats, String.class), HttpStatus.OK);
    }

    @PatchMapping(path = "/ban/{id}")
    public ResponseEntity<?> banUser(@PathVariable Integer id) {
        Optional<User> userOpt = userRepository.findById(id);

        if (!userOpt.isPresent()) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        User user = userOpt.get();
        if (user instanceof Admin && !isSuperAdmin()) {
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
        }

        Customer cust = (Customer) user;
        // todo set inactive

        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/artist/")
    public ResponseEntity<?> addArtistManually(@RequestBody Artist artist) {
        Optional<Artist> optArtist = artistRepository.findByName(artist.getName());

        if (optArtist.isPresent()) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        Artist saved = artistRepository.save(artist);

        return new ResponseEntity<Artist>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/artist/{id}")
    public ResponseEntity<?> rmArtistManually(@PathVariable Integer id) {
        Optional<Artist> optArtist = artistRepository.findById(id);

        if (!optArtist.isPresent()) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping(path = "/album/")
    public ResponseEntity<?> addAlbumManually(@RequestBody Album album) {
        Album saved = albumRepository.save(album);

        return new ResponseEntity<Album>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/album/{id}")
    public ResponseEntity<?> rmAlbumManually(@PathVariable Integer id) {
        Optional<Album> optArtist = albumRepository.findById(id);

        if (!optArtist.isPresent()) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public Admin getAdminById(Integer id) {
        Optional<Admin> admin = adminRepo.findById(id);
        if (!admin.isPresent()) {
            return null;
        }
        return admin.get();
    }

    private boolean isSuperAdmin() {
        User user = authFacade.getCurrentUser();
        if (!(user instanceof Admin)) {
            return false;
        }
        Admin admin = (Admin) user;
        return admin.isSuperAdmin();
    }
}
