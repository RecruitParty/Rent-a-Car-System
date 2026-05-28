package search;

import java.util.List;

import model.Spot;

public interface SpotDAO {
	// 존재하는 지점 조회
    List<Spot> getAllSpots();
}