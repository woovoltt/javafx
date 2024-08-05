package com.itgroup.dao;

import com.itgroup.bean.Karaoke;
import com.itgroup.utility.Paging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KaraokeDao extends SuperDao{


    public List<Karaoke> getPaginationData(Paging pageInfo) {
        Connection conn = null;
        String sql = " select id, songname, singer, genre, songdate, url ";
        sql += " from ( ";
        sql += " select id, songname, singer, genre, songdate, url,";
        sql += " rank() over(order by id) as ranking ";
        sql += " from karaoke ";

        String mode = pageInfo.getMode();
        boolean bool = mode.equals(null) || mode.equals("null") || mode.equals("") || mode.equals("all");

        if (!bool) {
            sql += " where genre = ? ";
        }

        sql += " ) ";
        sql += " where ranking between ? and ? ";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Karaoke> allData = new ArrayList<>();
        try {
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);

            if(!bool) {
                pstmt.setString(1, mode);
                pstmt.setInt(2, pageInfo.getBeginRow());
                pstmt.setInt(3, pageInfo.getEndRow());
            } else {
                pstmt.setInt(1, pageInfo.getBeginRow());
                pstmt.setInt(2, pageInfo.getEndRow());
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Karaoke bean = this.makeBean(rs);
                allData.add(bean);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return allData;
    }

    public String getUrl(int idx) {
        String url = "";
        Connection conn = null;
        String sql = " select id, songname, singer, genre, songdate, url ";
        sql += " from karaoke ";
        sql += " where id = ? ";


        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, String.valueOf(idx));



            rs = pstmt.executeQuery();

            while (rs.next()) {
                Karaoke bean = this.makeBean(rs);
                url = bean.getUrl();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return url;
    }



    private Karaoke makeBean(ResultSet rs) {
        Karaoke bean = new Karaoke();
        try {
            bean.setId(rs.getInt("id"));
            bean.setSongname(rs.getString("songname"));
            bean.setSinger(rs.getString("singer"));
            bean.setGenre(rs.getString("genre"));
            bean.setSongdate(rs.getString("songdate"));
            bean.setUrl(rs.getString("url"));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return bean;
    }


    public int deleteData(int id) {
        System.out.println("기본 키 = " + id);
        int cnt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " delete from karaoke ";

        sql += " where id = ?";

        try {
            conn = super.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);

            cnt = pstmt.executeUpdate();

            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return cnt;
    }

    public int getTotalCount(String mode) {
        int totalCount = 0;

        String sql = " select count(*) as mycnt from karaoke ";

        boolean bool = mode == null || mode.equals("all");
        if (!bool) {
            sql += " where genre = ? ";
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);
            if (!bool) {
                pstmt.setString(1, mode);
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                totalCount = rs.getInt("mycnt");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return totalCount;
    }

    public int insertData(Karaoke bean) {
        System.out.println(bean);
        int cnt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = " insert into karaoke(id, songname, singer, genre, songdate, url) values(karaokeId.NEXTVAL, ?, ?, ?, ?, ?)";

        try {
            conn = super.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, bean.getId());
            pstmt.setString(2, bean.getSongname());
            pstmt.setString(3, bean.getSinger());
            pstmt.setString(4, bean.getGenre());
            pstmt.setString(5, bean.getSongdate());
            pstmt.setString(6, bean.getUrl());

            cnt = pstmt.executeUpdate();

            conn.commit();
        }catch (Exception ex){
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return cnt;
    }

    public int updateData(Karaoke bean) {
        System.out.println(bean);
        int cnt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " update karaoke set songname = ?, singer = ?, genre = ?, songdate = ?, url = ? ";
        sql += " where id = ?";

        try {
            conn = super.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, bean.getSongname());
            pstmt.setString(2, bean.getSinger());
            pstmt.setString(3, bean.getGenre());
            pstmt.setString(4, bean.getSongdate());
            pstmt.setString(5, bean.getUrl());
            pstmt.setInt(6, bean.getId());

            cnt = pstmt.executeUpdate();

            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return cnt;
    }
}
