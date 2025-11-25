import { useState, useEffect } from "react";
import axios from "axios";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import { Card, CardContent } from "@/components/ui/card";
import { ThumbsUp, Send } from "lucide-react";
import { useDateFormatter } from "@/hooks/useDateFormatter";

interface Comment {
  id: number;
  text: string;
  writerName: string;
  writerEmail: string;
  writerId: number;
  createdAt: string;
  reportCount: number;
  likesCount: number;
}

interface CommentsSectionProps {
  comments?: Comment[];
  onSubmitComment?: (text: string) => void;
  userId?: string;
}


const CommentItem = ({ comment, userId }: { comment: Comment; userId?: string }) => {
  const { formatDate } = useDateFormatter();
  const [liked, setLiked] = useState(false);
  const [likeCount, setLikeCount] = useState(comment.likesCount);
  
  const avatarUrl = `${import.meta.env.VITE_API_URL}/api/v1/users/profile-image?email=${comment.writerEmail}`;

  const handleLike = async () => {
    if (!userId) return;
    
    try {
      await axios.post(`${import.meta.env.VITE_API_URL}/api/v1/comments/${comment.id}/like?userId=${userId}`);
      
      if (liked) {
        setLikeCount(likeCount - 1);
      } else {
        setLikeCount(likeCount + 1);
      }
      setLiked(!liked);
    } catch (error) {
      // Silently fail - no toast message
      console.error("Error liking comment:", error);
    }
  };

  return (
    <div>
      <div className="flex gap-3">
        <Avatar className="w-10 h-10">
          <AvatarImage 
            src={avatarUrl}
            alt={comment.writerName}
          />
          <AvatarFallback>{comment.writerName?.charAt(0) || "?"}</AvatarFallback>
        </Avatar>
          <div className="flex-1 space-y-2">
            <div>
              <div className="flex items-baseline gap-2">
                <span className="font-semibold text-sm">{comment.writerName}</span>
                <span className="text-xs text-muted-foreground">
                  {formatDate(comment.createdAt, "PPp")}
                </span>
              </div>
              <p className="text-sm mt-1">{comment.text}</p>
            </div>
          <div className="flex items-center gap-4">
            <button 
              onClick={handleLike}
              className={`flex items-center gap-1 text-sm hover:text-primary transition-colors ${
                liked ? 'text-primary font-semibold' : 'text-muted-foreground'
              }`}
            >
              <ThumbsUp className={`w-4 h-4 ${liked ? 'fill-current' : ''}`} />
              {likeCount}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

const CommentsSection = ({ comments = [], onSubmitComment, userId }: CommentsSectionProps) => {
  const [newComment, setNewComment] = useState("");

  const handleSubmit = () => {
    if (onSubmitComment && newComment.trim()) {
      onSubmitComment(newComment);
      setNewComment("");
    }
  };

  return (
    <Card>
      <CardContent className="p-6 space-y-6">
        <h2 className="text-xl font-bold">Sessão de Comentários</h2>
        
        <div className="space-y-4">
          <Textarea 
            placeholder="Escreva um comentário..." 
            className="min-h-[100px]"
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
          />
          <div className="flex items-center justify-between">
            <div className="flex gap-2 text-2xl">
              <button className="hover:scale-125 transition-transform">😊</button>
              <button className="hover:scale-125 transition-transform">😐</button>
              <button className="hover:scale-125 transition-transform">😢</button>
              <button className="hover:scale-125 transition-transform">😡</button>
            </div>
            <Button 
              className="bg-primary text-primary-foreground hover:bg-primary/90"
              onClick={handleSubmit}
            >
              <Send className="w-4 h-4 mr-2" />
              Enviar
            </Button>
          </div>
        </div>

        <div className="space-y-6 pt-4">
          {comments.map((comment) => (
            <CommentItem key={comment.id} comment={comment} userId={userId} />
          ))}
        </div>
      </CardContent>
    </Card>
  );
};

export default CommentsSection;
